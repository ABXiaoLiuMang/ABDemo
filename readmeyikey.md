https://blog.csdn.net/bskfnvjtlyzmv867/article/details/70849322用法介绍

// 启动新的Fragment，启动者和被启动者是在同一个栈的
start(SupportFragment fragment)

// 以某种启动模式，启动新的Fragment
start(SupportFragment fragment, int launchMode)

// 启动新的Fragment，并能接收到新Fragment的数据返回
startForResult(SupportFragment fragment,int requestCode)

// 启动目标Fragment，并关闭当前Fragment；不要尝试pop()+start()，动画会有问题
startWithPop(SupportFragment fragment)
---------------------

// 当前Fragment出栈(在当前Fragment所在栈内pop)
pop();

// 出栈某一个Fragment栈内之上的所有Fragment
popTo(Class fragmentClass/String tag, boolean includeSelf);

// 出栈某一个Fragment栈内之上的所有Fragment。如果想出栈后，紧接着.beginTransaction()开始一个新事务，
//请使用下面的方法， 防止多事务连续执行的异常
popTo(Class fragmentClass, boolean includeSelf, Runnable afterTransaction);
---------------------
// 获取所在栈内的栈顶Fragment
getTopFragment();

// 获取当前Fragment所在栈内的前一个Fragment
getPreFragment();

// 获取所在栈内的某个Fragment，可以是xxxFragment.Class，也可以是tag
findFragment(Class fragmentClass/String tag);
---------------------
startForResult用法
public class DetailFragment extends SupportFragment{

  private void goDetail(){
      // 启动ModifyDetailFragment
      startForResult(ModifyDetailFragment.newInstance(mTitle), REQ_CODE);
  }

  // ModifyDetailFragment调用setFragmentResult()后，在其出栈时，会回调该方法
  @Override
  public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
      super.onFragmentResult(requestCode, resultCode, data);
      if (requestCode == REQ_CODE && resultCode == RESULT_OK ) {
          // 在此通过Bundle data 获取返回的数据
      }
  }
}

public class ModifyTitleFragment extends SupportFragment{
    // 设置传给上个Fragment的bundle数据
    private void setResult(){
        Bundle bundle = new Bundle();
        bundle.putString("title", "xxxx");
        setFramgentResult(RESULT_OK, bundle);
    }
}
---------------------
