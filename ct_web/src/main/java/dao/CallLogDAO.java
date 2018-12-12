package dao;

import bean.CallLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
*
*@author ***
*@date
*@本类实现的功能为将查询出来的数据进行封装，与数据库进行交互
 * repository跟@Service,@Compent,@Controller这4种注解是没什么本质区别,都是声明作用
 * 因为被这些注解修饰的类就会被Spring扫描到并注入到Spring的bean容器中。
*/
@Repository
public class CallLogDAO {
    /**
    *
    *@author ***
    *@date
    *@查询数据库的话需要用到jdbc,自动扫描，自动装在
     * namedParameterJdbcTemplate可以为命名参数设置值
     *
    */
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate ;

    /**
    *@author
    *用户发过来的字段通过paramMap集合传进来，用户通过服务器进行查询，将查询后的结果封装到
     * calllog的对象中返回
    */
    public List<CallLog> getcallloglist(HashMap<String,String> paramsMap){


        int month1=0;

        String sql=null;
       Set<String> set=paramsMap.keySet();
        List <String> list1 = new ArrayList<String>(set);
        month1=Integer.valueOf(paramsMap.get(list1.get(0))).intValue();

        if(month1==-1){
            sql="SELECT `call_sum`, `call_duration_sum`, `telephone`, `name`, `year` , `month`, `day` FROM tb_dimension_date t4 INNER JOIN ( SELECT `id_date_dimension`, `call_sum`, `call_duration_sum`, `telephone`, `name` FROM tb_call t2 INNER JOIN ( SELECT `id`, `telephone`, `name` FROM tb_contacts WHERE telephone = :telephone ) t1 ON t2.id_contact = t1.id ) t3 ON t4.id = t3.id_date_dimension WHERE `year` = :year AND `month` != :month AND `day` = :day ORDER BY `year`, `month`;";
        }else{
            sql="SELECT `call_sum`, `call_duration_sum`, `telephone`, `name`, `year` , `month`, `day` FROM tb_dimension_date t4 INNER JOIN ( SELECT `id_date_dimension`, `call_sum`, `call_duration_sum`, `telephone`, `name` FROM tb_call t2 INNER JOIN ( SELECT `id`, `telephone`, `name` FROM tb_contacts WHERE telephone = :telephone ) t1 ON t2.id_contact = t1.id ) t3 ON t4.id = t3.id_date_dimension WHERE `year` = :year AND `month` = :month AND `day` != :day ORDER BY `year`, `month`;";
        }
        BeanPropertyRowMapper<CallLog> beanPropertyRowMapper = new BeanPropertyRowMapper<>(CallLog.class);
        List<CallLog> list = namedParameterJdbcTemplate.query(sql, paramsMap, beanPropertyRowMapper);
        return list;
    }

}
