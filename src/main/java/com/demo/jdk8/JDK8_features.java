package com.demo.jdk8;

import com.demo.jdk8.funinterface.IFunctionalService1;
import com.demo.jdk8.funinterface.IFunctionalService2;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2018/12/26
 * @time 10:51
 */
public class JDK8_features {

    public List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);

    /**
     * 1.Lambda表达式
     */
    @Test
    public void testLambda(){
        list.forEach(System.out::println);
        System.out.println();
        System.out.println("方式二：");
        list.forEach(e -> System.out.print(e + "\t"));
    }

    /**
     * 2.Stream函数式操作流元素集合
     */
    @Test
    public void testStream(){
        List<Integer> nums = Arrays.asList(1,1,null,2,3,4,null,5,6,7,8,9,10);
        System.out.println("求和："+ nums
                /**转成Stream */
                .stream()
                /**过滤 */
                .filter(team -> team!=null)
                /**去重 */
                .distinct()
                /**map操作 */
                .mapToInt(num->num*2)
                /**跳过前2个元素 */
                .skip(2)
                /**限制取前4个元素 */
                .limit(4)
                /**流式处理对象函数 */
                .peek(System.out::println)
                .sum());//
    }

    /**
     * 3.接口新增：默认方法与静态方法
     *  default 接口默认实现方法是为了让集合类默认实现这些函数式处理，而不用修改现有代码
     *  （List继承于Iterable<T>，接口默认方法不必须实现default forEach方法）
     */
    @Test
    public void testDefaultFunctionInterface(){
        /**可以直接使用接口名.静态方法来访问接口中的静态方法 */
        IFunctionalService1.staticMethod();
        /**接口中的默认方法必须通过它的实现类来调用 */
        new IFunctionalServiceImpl1().defaultMethod();
        /**多实现类，默认方法重名时必须复写 */
        new IFunctionalServiceImpl2().defaultMethod();
    }

    public class IFunctionalServiceImpl1 implements IFunctionalService1 {
        @Override
        public String getTag() {
            return "ifunc1";
        }
        /**实现接口后，因为默认方法不是抽象方法，重写/不重写都成！ */
//        @Override
//        public void defaultMethod(){
//            System.out.println("接口中的默认方法");
//        }
    }

    public class IFunctionalServiceImpl2 implements IFunctionalService1, IFunctionalService2 {
        @Override
        public String getTag() {
            return "ifunc2";
        }

        /**实现接口后，默认方法名相同，必须复写默认方法 */
        @Override
        public void defaultMethod() {
            /**接口的 */
            IFunctionalService1.super.defaultMethod();
            System.out.println("实现类复写重名默认方法！！！！");
        }
    }

    /**
     * 4.方法引用,与Lambda表达式联合使用
     */
    @Test
    public void testMethodReference(){
        /**构造器引用。语法是Class::new，或者更一般的Class< T >::new，要求构造器方法是没有参数； */
        final Car car = Car.create( Car::new );
        final List< Car > cars = Arrays.asList( car );
        /**静态方法引用。语法是Class::static_method，要求接受一个Class类型的参数； */
        cars.forEach( Car::collide );
        /**任意对象的方法引用。它的语法是Class::method。无参，所有元素调用； */
        cars.forEach( Car::repair );
        /**特定对象的方法引用，它的语法是instance::method。有参，在某个对象上调用方法，将列表元素作为参数传入； */
        final Car police = Car.create( Car::new );
        cars.forEach( police::follow );
    }

    public static class Car {
        public static Car create( final Supplier< Car > supplier ) {
            return supplier.get();
        }

        public static void collide( final Car car ) {
            System.out.println( "静态方法引用 " + car.toString() );
        }

        public void repair() {
            System.out.println( "任意对象的方法引用 " + this.toString() );
        }

        public void follow( final Car car ) {
            System.out.println( "特定对象的方法引用 " + car.toString() );
        }
    }

    /**
     * 5.引入重复注解
     * 1.@Repeatable
     * 2.可以不用以前的“注解容器”写法，直接写2次相同注解即可
     *
     * Java 8在编译器层做了优化，相同注解会以集合的方式保存，因此底层的原理并没有变化。
     */
//    @Test
//    public void RepeatingAnnotations(){
//        RepeatingAnnotations.main(null);
//    }

    /**
     * 6.类型注解
     * 新增类型注解:ElementType.TYPE_USE 和ElementType.TYPE_PARAMETER（在Target上）
     *
     */
//    @Test
//    public void ElementType(){
//        Annotations.main(null);
//    }

    /**
     * 7.最新的Date/Time API (JSR 310)
     */
    @Test
    public void DateTime(){
        /**1.Clock */
        final Clock clock = Clock.systemUTC();
        System.out.println( clock.instant() );
        System.out.println( clock.millis() );

        /**2. ISO-8601格式且无时区信息的日期部分 */
        final LocalDate date = LocalDate.now();
        final LocalDate dateFromClock = LocalDate.now( clock );

        System.out.println( date );
        System.out.println( dateFromClock );

        /** ISO-8601格式且无时区信息的时间部分 */
        final LocalTime time = LocalTime.now();
        final LocalTime timeFromClock = LocalTime.now( clock );

        System.out.println( time );
        System.out.println( timeFromClock );

        /** 3.ISO-8601格式无时区信息的日期与时间 */
        final LocalDateTime datetime = LocalDateTime.now();
        final LocalDateTime datetimeFromClock = LocalDateTime.now( clock );

        System.out.println( datetime );
        System.out.println( datetimeFromClock );

        /** 4.特定时区的日期/时间， */
        final ZonedDateTime zonedDatetime = ZonedDateTime.now();
        final ZonedDateTime zonedDatetimeFromClock = ZonedDateTime.now( clock );
        final ZonedDateTime zonedDatetimeFromZone = ZonedDateTime.now( ZoneId.of( "America/Los_Angeles" ) );

        System.out.println( zonedDatetime );
        System.out.println( zonedDatetimeFromClock );
        System.out.println( zonedDatetimeFromZone );

        /**5.在秒与纳秒级别上的一段时间 */
        final LocalDateTime from = LocalDateTime.of( 2014, Month.APRIL, 16, 0, 0, 0 );
        final LocalDateTime to = LocalDateTime.of( 2015, Month.APRIL, 16, 23, 59, 59 );

        final Duration duration = Duration.between( from, to );
        System.out.println( "Duration in days: " + duration.toDays() );
        System.out.println( "Duration in hours: " + duration.toHours() );
    }

    /**
     * 8.新增base64加解密API
     */
    @Test
    public void testBase64(){
        final String text = "就是要测试加解密！！abjdkhdkuasu!!@@@@";
        String encoded = Base64.getEncoder()
                .encodeToString( text.getBytes( StandardCharsets.UTF_8 ) );
        System.out.println("加密后="+ encoded );

        final String decoded = new String(
                Base64.getDecoder().decode( encoded ),
                StandardCharsets.UTF_8 );
        System.out.println( "解密后="+decoded );
    }

    /**
     * 9.数组并行（parallel）操作
     */
    @Test
    public void testParallel(){
        long[] arrayOfLong = new long [ 20000 ];
        /**1.给数组随机赋值 */
        Arrays.parallelSetAll( arrayOfLong,
                index -> ThreadLocalRandom.current().nextInt( 1000000 ) );
        /**2.打印出前10个元素 */
        Arrays.stream( arrayOfLong ).limit( 10 ).forEach(
                i -> System.out.print( i + " " ) );
        System.out.println();
        /**3.数组排序 */
        Arrays.parallelSort(arrayOfLong);
        /**4.打印排序后的前10个元素 */
        Arrays.stream( arrayOfLong ).skip(arrayOfLong.length-11).limit( 10 ).forEach(
                i -> System.out.print( i + " " ) );
        System.out.println();
    }

    /**
     * 10.JVM的PermGen空间被移除：取代它的是Metaspace（JEP 122）元空间
     */
    @Test
    public void testMetaspace(){
        //-XX:MetaspaceSize初始空间大小，达到该值就会触发垃圾收集进行类型卸载，同时GC会对该值进行调整
        //-XX:MaxMetaspaceSize最大空间，默认是没有限制
        //-XX:MinMetaspaceFreeRatio在GC之后，最小的Metaspace剩余空间容量的百分比，减少为分配空间所导致的垃圾收集
        //-XX:MaxMetaspaceFreeRatio在GC之后，最大的Metaspace剩余空间容量的百分比，减少为释放空间所导致的垃圾收集
    }

}

