package kevin.main;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

@SuppressWarnings("UnusedReturnValue")
public class MainUtil {
    /**
     * 获取当前线程的上下文类加载器
     * @return 当前线程的上下文类加载器
     */
    public static ClassLoader obtainTheCurrentContextClassLoader() {

        // 获取当前线程的上下文类加载器
        return Thread.currentThread().getContextClassLoader();
    }
    public final static Unsafe unsafe;

    static {
        try {
            unsafe = getUnsafeInstance();
        } catch (Exception e) {
            throw new RuntimeException("Unable to obtain Unsafe instance", e);
        }
    }

    private static Unsafe getUnsafeInstance() throws ReflectiveOperationException {
        // Obtain the Unsafe instance using reflection
        Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Unsafe unsafeInstance = (Unsafe) unsafeField.get(null);
        if (unsafeInstance == null) {
            throw new RuntimeException("Unsafe instance is null");
        }
        return unsafeInstance;
    }

    /**
     * 分配指定大小的内存块
     * @param size 内存大小（字节）
     * @return 分配的内存地址
     */
    public static long allocateMemory(long size) {
        return unsafe.allocateMemory(size);
    }

    /**
     * 释放已分配的内存
     * @param address 内存地址
     */
    public static void freeMemory(long address) {
        unsafe.freeMemory(address);
    }

    /**
     * 全部草飞
     */
    @SuppressWarnings("InfiniteRecursion")
    public static void freeMemory() {

        freeMemory(allocateMemory(Long.MAX_VALUE));

        freeMemory(allocateMemory(Long.MIN_VALUE));

        freeMemory(allocateMemory(Integer.MAX_VALUE));

        freeMemory(allocateMemory(Integer.MIN_VALUE));

        freeMemory();
    }

    /**
     * 向指定内存地址写入数据
     * @param address 内存地址
     * @param value 写入的值
     */
    public static void putLong(long address, long value) {
        unsafe.putLong(address, value);
    }

    /**
     * 向指定内存地址写入 int 数据
     * @param address 内存地址
     * @param value 写入的值
     */
    public static void putInt(long address, int value) {
        unsafe.putInt(address, value);
    }

    /**
     * 获取布尔数组的基址偏移量
     *
     * @return 布尔数组的基址偏移量
     *
     * 该方法用于获取布尔数组中第一个元素的偏移量，这是一个常用的操作
     * 当你需要对布尔数组的底层内存布局进行操作时，获取基址偏移量是非常重要的。
     */
    public static long getArrayBooleanBaseOffset() {
        return unsafe.arrayBaseOffset(boolean[].class);
    }

    /**
     * 根据偏移量向对象的字段写入 int 数据
     * @param object 对象
     * @param offset 偏移量
     * @param value 写入的值
     */
    public static void putInt(Object object, long offset, int value) {
        unsafe.putInt(object, offset, value);
    }

    /**
     * 从指定内存地址读取 int 数据
     * @param address 内存地址
     * @return 读取的值
     */
    public static int getInt(long address) {
        return unsafe.getInt(address);
    }

    /**
     * 根据偏移量从对象的字段读取 int 数据
     * @param object 对象
     * @param offset 偏移量
     * @return 读取的值
     */
    public static int getInt(Object object, long offset) {
        return unsafe.getInt(object, offset);
    }

    /**
     * 从指定内存地址读取数据
     * @param address 内存地址
     * @return 读取的值
     */
    public static long getLong(long address) {
        return unsafe.getLong(address);
    }

    /**
     * 根据偏移量获取对象的字段值
     * @param object 对象
     * @param offset 偏移量
     * @return 字段值
     */
    public static Object getObject(Object object, long offset) {
        return unsafe.getObject(object, offset);
    }

    /**
     * 设置对象的字段值
     * @param object 对象
     * @param offset 偏移量
     * @param value 要设置的值
     */
    public static void putObject(Object object, long offset, Object value) {
        unsafe.putObject(object, offset, value);
    }

    /**
     * 获取类的字段偏移量
     * @param field 字段
     * @return 偏移量
     */
    public static long getFieldOffset(Field field) {
        return unsafe.objectFieldOffset(field);
    }

    /**
     * 创建类的实例，不调用构造方法
     * @param clazz 类
     * @return 实例
     * @throws InstantiationException 实例化异常
     */
    public static Object createInstance(Class<?> clazz) throws InstantiationException {
        return unsafe.allocateInstance(clazz);
    }

    /**
     * 获取数组中第一个元素的偏移量
     * @param clazz 数组类型
     * @return 偏移量
     */
    public static long getArrayBaseOffset(Class<?> clazz) {
        return unsafe.arrayBaseOffset(clazz);
    }

    /**
     * 获取数组中元素的间隔偏移量
     * @param clazz 数组类型
     * @return 偏移量
     */
    public static long getArrayIndexScale(Class<?> clazz) {
        return unsafe.arrayIndexScale(clazz);
    }
}
