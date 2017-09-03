#定义命名空间，java com.lavor.thrift表示转换成Java的包名
#编译thrift文件时，还会将生成的java文件放到包名对应的目录中
namespace java com.lavor.thrift
/**
* 定义服务service相当于Java中的接口interface，但是生成对应的java文件时，生成的是类class
* 不过该服务里面只能定义方法（方法没有方法体），不能定义字段
**/
service MyService{
    #void与Java中的void作用相同
    void plus(1:i32 x,2:i32 y);//1:表示的是thriftId是1，方便查找该数据，不使用时，默认从-1开始递减
    i32 minus(1:i32 x,2:i32 y);
}
/**
* 定义结构体struct相当于Java中的JavaBean
**/
struct MyBean{
    bool boolearnType;//bool相当于Java中的boolearn
    byte byteType;//byte相当于Java中的byte
    i16 shortType;//shortType相当于Java中的short
    i32 intType;//intType相当于Java中的int
    i64 longType;//bool相当于Java中的boolearn
    double doubleType;//double相当于Java中的double
    #thrift中没有char与float类型，并且不能设置数字类型为无符号类型
    string stringType;//string相当于Java中的String
    slist stringListType;//slist也是对应着String
    #list、set、map中的基本类型会在Java中会自动转换成对应的包装类型
    list<i32> arrayListType;//list相当于Java中的ArrayList
    set<string> hashSetType;//set相当于Java中的HashSet
    map<string,string> hashMapType;//map相当于Java中的HashMap
    binary bufferType;//binary相当于Java中的ByteBuffer
    1: i32 number;//1:表示的是thriftId是1，方便查找该数据，不使用时，默认从-1开始递减
}
/**
* enum相当于Java中的enum，是枚举
**/
enum MyEnum{
    ONE;
    TWO;
    Three;
}
/**
* thrift还可以用const修饰常量，相当于Java中的final
* exception相当于Java中的Exception异常
* typedef可以为类型定义别名
**/