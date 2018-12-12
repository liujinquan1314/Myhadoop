package controller;

import bean.CallLog;
import bean.QueryInfo;
import dao.CallLogDAO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//相当于mvc中的c
@Controller
public class CallLogHandler {

    @RequestMapping("/queryCallLogList")
    public String queryCallLog(Model model, QueryInfo queryInfo){
        //获得配置文件
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
       //获得callLog对象
        CallLogDAO callLogDAO = applicationContext.getBean(CallLogDAO.class);
        //前端的数据添加到hashMap的映射
        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("telephone", queryInfo.getTelephone());
        hashMap.put("year", queryInfo.getYear());
        hashMap.put("month", queryInfo.getMonth());
        hashMap.put("day", queryInfo.getDay());


        int month1=0;
        int day1=0;
        String imf="时间格式错误！重新输入！";

        Set<String> set=hashMap.keySet();
        List <String> list1 = new ArrayList<String>(set);
        month1=Integer.valueOf(hashMap.get(list1.get(0))).intValue();
        day1=Integer.valueOf(hashMap.get(list1.get(3))).intValue();

        if(month1>12||day1>31){

            model.addAttribute("information",imf);
            return "index";

        }
        //传入参数获得查询集合
        List<CallLog> list = callLogDAO.getcallloglist(hashMap);

        StringBuilder dateSB = new StringBuilder();
        StringBuilder callSumSB = new StringBuilder();
        StringBuilder callDurationSumSB = new StringBuilder();

        if(month1==-1){

            for(int i = 0; i < list.size(); i++){
                CallLog callLog = list.get(i);

                dateSB.append(callLog.getMonth() + "月,");
                callSumSB.append(callLog.getCall_sum() + ",");
                callDurationSumSB.append(callLog.getCall_duration_sum() + ",");
            }
        }else{
            for(int i = 0; i < list.size(); i++){
                CallLog callLog = list.get(i);

                dateSB.append(callLog.getDay() + "日,");
                callSumSB.append(callLog.getCall_sum() + ",");
                callDurationSumSB.append(callLog.getCall_duration_sum() + ",");
            }
        }




        dateSB.deleteCharAt(dateSB.length()-1);
        callSumSB.deleteCharAt(callSumSB.length()-1);
        callDurationSumSB.deleteCharAt(callDurationSumSB.length()-1);

        //通过model返回数据
        model.addAttribute("telephone", list.get(0).getTelephone());
        model.addAttribute("name", list.get(0).getName());
        model.addAttribute("date", dateSB.toString());
        model.addAttribute("count", callSumSB.toString());
        model.addAttribute("duration", callDurationSumSB.toString());

        return "jsp/CallLogListEchart";
    }
}
