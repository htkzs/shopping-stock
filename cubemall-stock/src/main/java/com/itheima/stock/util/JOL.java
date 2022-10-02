package com.itheima.stock.util;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;
//查看一个对象的组成部分
@Slf4j(topic = "e")
public class JOL {
    public static void main(String[] args) {
        A a = new A();
        log.debug( ClassLayout.parseInstance(a).toPrintable());
        ClassLayout.parseInstance(a).toPrintable(a); }
}
