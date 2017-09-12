package com.thinkgem.jeesite.modules.quartz.net;

import com.thinkgem.jeesite.modules.quartz.util.pdd.HttpClientUtil;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huanghengmin on 2017/9/10.
 */
public class Check {

    private static final String path_login = "http://web-dx.3600d.net:83/w1722247/api.php?name=zdy_login";
    private static final String path_zx = "http://web-dx.3600d.net:83/w1722247/api.php?name=zdy_zx";
    private static final String path_kd = "http://web-dx.3600d.net:83/w1722247/api.php?name=zdy_kd";
    private static final String path_pay = "http://web-dx.3600d.net:83/w1722247/api.php?name=zdy_pay";

    /*接口名称：login
    中文名称：登录
    接口作用：使用用户注册的账号或充值卡号登录
    参数数量：7
    接口原型：login([软件编号],[账号],[密码],[软件版本],[机器码],[游戏号],[返回信息值])<返回错误信息和指定及信息值>
    参数1：软件的编号，如：0001,0002,0003.....
    参数2：用户账号
    参数3：MD5一次加密的用户密码
    参数4：软件版本，软件的版本号，用于判断软件强制更新时使用
    参数5：机器码，用于控制用户异地登录
    参数6：游戏号，用户卡密直接登录，并且需要绑定游戏号时才需要填写
    参数7：返回信息值，1=剩余秒数，2=剩余点数，3=允许几开,4=游戏号，5=上次登陆ip，6=邮箱，7=上次登录机器码，8=上次登录时间，9=用户权限，10=验证码(用于取回附加字符的必须参数)，11=到期时间，12=用户备注，13=注册时间
    自定义接口中的调用方法：

    function zdy_login($rjbh,$zh,$mm,$bb,$jqm,$yxh,$xxz){//请将 ApiName 改为您自己想要定义的任意名称
        $data=login($rjbh,$zh,$mm,$bb,$jqm,$yxh,$xxz);
        return $data;
    }*/




    public static String zdy_login(String c2) {

        Map<String, Object> stringMap = new HashMap<String, Object>();
        stringMap.put("c1", "10001");
        stringMap.put("c2", c2);
        stringMap.put("c3", "");
        stringMap.put("c4", "1.0");
        stringMap.put("c5", "jqm");
        stringMap.put("c6", "");
        stringMap.put("c7", "1,11");

        String response = null;
        try {
            response = HttpClientUtil.httpPostRequest(path_login, stringMap);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    接口名称：kd
    中文名称：扣点
    接口作用：查询、扣除用户点数
    参数数量：5
    接口原型：kd([软件编号],[账号],[密码],[操作类型],[点数])<返回用户剩余点数>
       参数1：软件的编号，如：0001,0002,0003.....
       参数2：用户账号
       参数3：一次MD5加密的用户密码
       参数4：操作类型，0=扣点，1=查询
       参数5：点数，若参数4为0，则扣除此数量的点数
    */
    public static String zdy_kd(String c2,String c4,String c5) {

        Map<String, Object> stringMap = new HashMap<String, Object>();
        stringMap.put("c1", "10001");
        stringMap.put("c2", c2);
        stringMap.put("c3", "");
        stringMap.put("c4", c4);
        stringMap.put("c5", c5);

        String response = null;
        try {
            response = HttpClientUtil.httpPostRequest(path_kd, stringMap);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    接口名称：pay
    中文名称：用户充值
    接口作用：使用充值卡给用户账号充值使用时间和点数
    参数数量：4
    接口原型：pay([软件编号],[账号],[推荐人账号],[卡号])<返回充值结果>
        参数1：软件的编号，如：0001,0002,0003.....
        参数2：用户账号
        参数3：推荐人账号，若填写推荐人账号的话，会根据推广政策赠送用户和推荐人使用时间、点数，（赠送设置见软件列表》杂项设置》推广赠送）
        参数4：卡号，充值卡密，可为一张或多张，多张之间请用小写逗号分隔开
    */
    public static String zdy_pay(String c2,String c4) {
        Map<String, Object> stringMap = new HashMap<String, Object>();
        stringMap.put("c1", "10001");
        stringMap.put("c2", c2);
        stringMap.put("c3", "");
        stringMap.put("c4", c4);

        String response = null;
        try {
            response = HttpClientUtil.httpPostRequest(path_pay, stringMap);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * 将字符串转成unicode
     * @param str 待转字符串
     * @return unicode字符串
     */
    public static String convert(String str)
    {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }

    public static String decodeUnicode(final String dataStr) {
        int start = 0;
        int end = 0;
        final StringBuffer buffer = new StringBuffer();
        while (start > -1) {
            end = dataStr.indexOf("\\u", start + 2);
            String charStr = "";
            if (end == -1) {
                charStr = dataStr.substring(start + 2, dataStr.length());
            } else {
                charStr = dataStr.substring(start + 2, end);
            }
            char letter = (char) Integer.parseInt(charStr, 16); // 16进制parse整形字符串。
            buffer.append(new Character(letter).toString());
            start = end;
        }
        return buffer.toString();
    }
    /*
    接口名称：zx
    中文名称：在线状态控制
    接口作用：当客户端以远程API接口[login]登录时，通过使用此接口来控制用户的登录数量，并且检测用户的在线、到期、封号等状态。
    参数数量：6
    接口原型：zx([软件编号],[用户账号],[用户密码MD5],[在线标识],[需要删除标识],[操作类型])<返回执行结果>
       参数1：软件编号，软件的编号，如：10001,10002,10003。。。
       参数2：用户账号，可以是用户注册的账号，也可以是卡密直接登陆
       参数3：用户密码，注册的账号的密码的MD5（小写），卡密直接登录的可以忽略此参数
       参数4：在线标识，用户在线的唯一标识，32位长度（一般用机器码等数据组合的MD5）
       参数5：需要删除的在线标识，当用户软件意外退出导致的在线标识没有被删除 ，为了防止顶掉正常的在线标识，
              所以需要把意外退出的标识删掉（当参数6为0时，将此标识写到本地文件，当参数6为2时将本地文件删除，
              下次参数6为0时，检测本地文件是否存在在线标识记录，如果有记录的话就把这个本地在线标识一起提交，
              这样就会增加新在线标识，删除旧标识，保证软件在线数量的准确性，防止在线的软件被踢掉）
       参数6：操作类型，
             0= 增加在线标识，调用Login接口登录成功后，使用此功能增加软件的在线标识，如果在线标识数量超出用户< /FONT>
              允许登录软件的数量， 则会删掉激活时间最早的在线标识（参数6的值为1时，将会刷新在线标识的激活时间到当前时间）
             1= 检测在线标识，软件登录成功后，软件正常运行的情况下，循环调用此功能来检测软件是否合法和 < /FONT>
               用户账号的封停、到期、被删除等状态。
             2= 删除在线标识，软件退出的时候执行此操作，删除在线标识，表示软件已经退出了，为相同账号登录其他软件时空出< /FONT>
              允许登录的数量 。
             3= 查询用户在线标识数量，这个功能一般用不到，显示账号登录软件的大概数量。< /FONT>
    执行结果 返回值：
     0 = 在线标识不存在，表示用户超出允许登录数量在线标识被删除了，或者用户登录成功后没有成功添加在线标识
     1 = 增加标识成功、在线标识正常、删除标识成功（对应参数6的值分别为 0 1 2 时操作成功的值）
     2 = 当参数6为0，增加新在线标识的时候，如果标识存在了，就会返回这个值
     -1 = 账号不存在
     -2 = 账号已经过期
     -3 = 账号被封
     -4 = 密码错误
     -100 = 未知错误，如：数据库故障、系统故障、文件损坏等问题造成的错误
     参数6为3时，返回账号在线标识的数量（不保证100%准确，因为有软件意外退出没删除在线标识的情况）

     function zdy_zx($rjbh,$user,$md5pass,$sbm,$sbm2,$lx){//请将 zdy_zx 改为您自己想要定义的任意名称
        $data=zx($rjbh,$user,$md5pass,$sbm,$sbm2,$lx);//调用系统API接口
        return $data;//返回数据
    }
    */
    public static String zdy_zx(String c1, String c2,String c3,String c4,String c5,String c6) {

        Map<String, Object> stringMap = new HashMap<String, Object>();
        stringMap.put("c1", c1);
        stringMap.put("c2", c2);
        stringMap.put("c3", c3);
        stringMap.put("c4", c4);
        stringMap.put("c5", c5);
        stringMap.put("c6", c6);

        String response = null;
        try {
            response = HttpClientUtil.httpPostRequest(path_zx, stringMap);
            return response;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void main(String args[])throws Exception{
        String c1 = "10001";
//        String c2 = "DDDD731DB7EE6A933F5F";
        String c2 = "aaa";
        String c3 = "";
        String c4 = "1.0";
        String c5 = "jqm";
        String c6 = "";
        String c7 = "1,11";
        String result = zdy_login(c2);
//        String[] ss = result.split("<\\|>");
        System.out.print(result);

        /*String c2 = "DDDDD86D02A6DFB3E9C5";
        String c4 = "1";
        String c5 = "0";
        String result = zdy_kd(c2,c4,c5);
        System.out.print(result);*/

      /*  String c1 = "10001";
        String c2 = "DDDD731DB7EE6A933F5F";
        String c3 = "";
        String c4 = "1.0";
        String c5 = "";
        String c6 = "0";
        zdy_zx(c1,c2,c3,c4,c5,c6);*/
    }
}
