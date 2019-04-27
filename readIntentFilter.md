1.action的匹配规则
 action 是一个字符串，action匹配规则是，intent中必须有一个action且必须能够和过滤规则中的某个action匹配

 注意：a : action中的内容是区分大小写的
       b : Intent中如果没有指定action，则视为匹配失败

2.category的匹配规则
  category是一个字符串，匹配规则是，intent中可以没有categoru,但是如果一旦有gategory,不关有几个，每个都要
  能够和过滤规则中的任何一个category匹配

  注意：如果想要A隐式启动B,那么需要B的intent-filter中指定android.intent,category.DEFAULT这个actegory,
  原因是系统在调用startActivity或者startActionForResult方法的时候会默认的为Intent加上android,intent.category.DEFAULT

3.data的匹配规则
 data的匹配规则:intent中必须含有data数据，并且data数据能够完全匹配过滤规则中的某一个data,data语法如下：
 <data android:scheme="string"
       android:host="string"
       android:port="string"
       android:pathPattern="string"
       android:pathPrefix="string"
       android:mine="string">
 data有两部分组成：mime和URI,URI通过如下格式，包括：scheme,host,port,path,pathPrefix和pathPattern

 <scheme>://<host>:<port>/[<path>|<pathPrefix>|<pathPattern>]

 mimeType:指定媒体类型，比如image/jpeg,audio/mpeg4-generic,vidio/*等，可以表示图片，文本，视频等不同的媒体格式
 scheme:URI的模式，比如http,file,content等，如果URI中没有指定scheme,那么这个URI中的其他参数无效，意味着这个URI是无效的
 host:URI的主机名，如www.baidu.com,如果host未指定，那么整个URI无效的，意味着这个URI是无效的
 port:URI中的端口号，比如80，当URI中指定scheme和host参数的时候，port参数才是有意义的
 pathPrefix ：表述路径的前缀信息。
 pathPattern ：表述路径的完整信息，但它里面可以包含通配符 * ，表示0个或任意字符（如果想要表示真是字符串，则需要转义成 \\* ； \ 要写成 \\\\ ）。
 我们可以通过 intent.setDataAndType(Uri.parse("URI字符串"), "mimeType字符串") 的格式为Intent设置data。

 注意：a:URI可以不设置，但是如果设置了，则scheme和host属性必须要设置
       b:URI的scheme属性有默认值，默认值为content或者file,因此，就算在intent-filter中没有为data设置URI,也需要在匹配的时候
         设置scheme和host两个属性，并scheme属性的值必须是content或者file
       c:在为intent指定data的时候，必须调用setDataAndType方法，不能先调用setData方法，在调用setType方法，因为这两个方法会彼此覆盖对方的值
       d:所有有关data的属性可以放在一个data标签中，也可以分作多个data标签存放，效果是一样的

4.总结
  1、以下是在JAVA代码中匹配某个Activity的 intent-filter 的代码：

  Intent intent = new Intent();
  intent.addAction("my.itgungnir.action1");
  intent.addCategory("my.itgungnir.category1");
  intent.setDataAndType(Uri.parse("file://abc"), "text/plain");
  startActivity(intent);
  2、在Menifest文件的<intent-filter>标签中，action、category和data都可以有多个；在JAVA代码中，一个Intent中只能有一个action和一个data，可以有多个category。

  3、我们在通过隐式方式启动一个Activity的时候，可以做以下判断，看有没有Activity能够匹配我们的Intent，具体的判断方法有两种：

  　（1）使用 PackageManager 或者 Intent 的 resolveActivity() 方法，这个方法会返回最佳匹配的Activity信息，如果没有匹配的Activity，则返回null；

  　（2）使用 PackageManager 的 queryIntentActivities() 方法，这个方法会返回所有成功匹配的Activity的信息。

  public abstract ResolveInfo resolveActivity(Intent intent, int flags);
  public abstract List<ResolveInfo> queryIntentActivities(Intent intent, int flags);
  　　需要说明的是这两个方法的第二个参数，我们在使用这两个方法的时候，第二个参数都必须是 Intent.MATCH_DEFAULT_ONLY ，这个参数用来匹配那些在 intent-filter 中声明了category为 android.intent.category.DEFAULT 的Activity，避免某些Activity因为没有设置category为DEFAULT而无法接收隐式Intent。

  4、如果想将一个Activity标记为应用的入口，可以在其 <intent-filter> 标签中添加如下两行属性（这两行属性必须同时存在才有用，缺一不可）：

  <action android:name="android.intent.action.MAIN" />
  <category android:name="android.intent.category.LAUNCHER" />
