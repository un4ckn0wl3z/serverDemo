package com.haoyun.playerMgt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RecursiveTask;

/**
 *
 * @author Administrator
 * @date 2017/7/6
 */
public class ForkJoinCalcPoolNum extends RecursiveTask<Map<String, Long>> {
    private static final int THRESHOLD_NUM = 1000;
    private List<MPlayer> playerList;

    public ForkJoinCalcPoolNum(List<MPlayer> playerList) {
        this.playerList = playerList;
    }

    @Override
    protected Map<String, Long> compute() {
        Map<String, Long> map = new HashMap<>();
        map.put("sum_rcNum", (long) 0);
        map.put("sum_goldNum", (long) 0);

        if (playerList == null || (playerList.size() < 1)) {
            return map;
        }

        long sum_rcNum = 0;
        long sum_goldNum = 0;
        //如果任务足够小就计算任务
        boolean canCompute = playerList.size() <= THRESHOLD_NUM;
        if (canCompute) {
            for (MPlayer m : playerList) {
                // System.out.println("roomCardNum: --------------" + m.getRoomCardNum());
                // System.out.println("goldNum: --------------" + m.getMoney());
                if (m.getRoomCardNum() != null) {
                    sum_rcNum += m.getRoomCardNum();
                }
                if (m.getMoney() != null) {
                    sum_goldNum += m.getMoney();
                }
            }

            map.put("sum_rcNum", sum_rcNum);
            map.put("sum_goldNum", sum_goldNum);
        } else {
            // 如果任务大于阈值，就分裂成两个子任务计算
            int middle = playerList.size() / 2;
            ForkJoinCalcPoolNum leftTask  = new ForkJoinCalcPoolNum(playerList.subList(0, middle));
            ForkJoinCalcPoolNum rightTask = new ForkJoinCalcPoolNum(playerList.subList(middle, playerList.size()));

            // 执行子任务
            leftTask.fork();
            rightTask.fork();

            //等待任务执行结束合并其结果
            Map<String, Long> map1 = leftTask.join();
            Map<String, Long> map2 = rightTask.join();

            //合并子任务
            map.put("sum_rcNum",   map1.get("sum_rcNum") + map2.get("sum_rcNum"));
            map.put("sum_goldNum", map1.get("sum_goldNum") + map2.get("sum_goldNum"));
        }

        return map;
    }
}
