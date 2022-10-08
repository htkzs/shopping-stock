JUC全链路优化方案
模拟远程服务调用并测试运行的时间
```java
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @RequestMapping("/order")
    public Object orderInfo(@RequestParam("userId") long userId) {
        return orderService.order(userId);
    }
}
```
>service层(多线程优化之前的代码)
```java
@Service
@Slf4j
public class OrderService {
    @Autowired
    private RemoteService remoteService;

    public Object order(long userId) {
        //第一个执行过程交由主线程执行
        long startTime = System.currentTimeMillis();
        OrderInfo orderInfo = remoteService.createOrder(userId);
        String jsonString = JSONObject.toJSONString(orderInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        System.out.println(orderInfo);
        System.out.println(jsonObject);
        GoodsInfo goodsInfo = remoteService.dealGoods(orderInfo);
        String goodsJsonString = JSONObject.toJSONString(goodsInfo);
        JSONObject goodsString = JSONObject.parseObject(goodsJsonString);
        JSONObject pointsJsonObject = remoteService.dealPoints(orderInfo);
        System.out.println(pointsJsonObject);
        JSONObject deliverJsonObject = remoteService.dealDeliver(orderInfo);
        try {
            jsonObject.putAll(goodsFuture.get());
            jsonObject.putAll(pointFuture.get());
            jsonObject.putAll(deliverFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        log.info("业务执行所需的总时间为：" + (endTime - startTime) + "ms");
        return jsonObject;
    }
}
```
>使用多线程优化上述方案
```java
@Service
@Slf4j
public class OrderService {
    @Autowired
    private RemoteService remoteService;

    public Object order(long userId) {
        //第一个执行过程交由主线程执行
        long startTime = System.currentTimeMillis();
        OrderInfo orderInfo = remoteService.createOrder(userId);
        String jsonString = JSONObject.toJSONString(orderInfo);
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        System.out.println(orderInfo);
        System.out.println(jsonObject);
        //创建一个子线程执行该任务 由于需要返回值故 使用callable+futureTusk方式
        Callable<JSONObject> goodsCall = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                GoodsInfo goodsInfo = remoteService.dealGoods(orderInfo);
                String goodsJsonString = JSONObject.toJSONString(goodsInfo);
                JSONObject goodsString = JSONObject.parseObject(goodsJsonString);

                return goodsString;
            }
        };
        FutureTask<JSONObject> goodsFuture = new FutureTask<JSONObject>(goodsCall);
        Thread thread1 = new Thread(goodsFuture);

        Callable<JSONObject> pointsCall = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                JSONObject pointsJsonObject = remoteService.dealPoints(orderInfo);
                System.out.println(pointsJsonObject);
                return pointsJsonObject;
            }
        };
        FutureTask<JSONObject> pointFuture = new FutureTask<JSONObject>(pointsCall);
        Thread thread2 = new Thread(pointFuture);

        Callable<JSONObject> deliverCall = new Callable<JSONObject>() {
            @Override
            public JSONObject call() throws Exception {
                JSONObject deliverJsonObject = remoteService.dealDeliver(orderInfo);
                System.out.println(deliverJsonObject);
                return deliverJsonObject;
            }
        };
        FutureTask<JSONObject> deliverFuture = new FutureTask<JSONObject>(deliverCall);
        Thread thread3 = new Thread(deliverFuture);
        /*
         此处会阻塞主线程 等待结果返回 tomcat线程会在这里阻塞等待结果的response
         */
        try {
            jsonObject.putAll(goodsFuture.get());
            jsonObject.putAll(pointFuture.get());
            jsonObject.putAll(deliverFuture.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        long endTime = System.currentTimeMillis();
        log.info("业务执行所需的总时间为：" + (endTime - startTime) + "ms");
        return jsonObject;
    }
}
```