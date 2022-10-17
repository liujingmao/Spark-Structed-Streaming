package com.imooc.web.service;

import com.imooc.web.domain.AccessUserHour;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.hbase.HbaseTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HBaseService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    public List<AccessUserHour> queryAccessUserHours(String start, String end) {

        List<AccessUserHour> accessUserHours = new ArrayList<>();

        Scan scan = new Scan();
        scan.setStartRow(start.getBytes());
        scan.setStopRow(end.getBytes());

        List<Result> results = hbaseTemplate.find("access_user_hour", scan, (rowMapper, rowNum) -> rowMapper);

        for(Result result :  results) {
            while(result.advance()){
                Cell cell = result.current();
                String rowkey = new String(CellUtil.cloneRow(cell));
                long time = Bytes.toLong(CellUtil.cloneValue(cell));

                AccessUserHour domain = new AccessUserHour();

                String[] splits = rowkey.split("_");
                domain.setHour(splits[0]);
                domain.setUser(splits[1]);
                domain.setTime(time/1000.0/60);

                accessUserHours.add(domain);
            }
        }
        return accessUserHours;
    }
}
