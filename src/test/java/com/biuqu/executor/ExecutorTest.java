//package com.biuqu.executor;
//
//import com.biuqu.csv.model.Csv;
//import com.biuqu.csv.queue.CsvBatchReader;
//import com.biuqu.csv.queue.CsvBatchWriter;
//import com.biuqu.csv.task.CsvAsyncExecutor;
//import com.biuqu.reader.RandomAccessFileReader;
//import com.biuqu.task.BaseBatchAsyncExecutor;
//import com.biuqu.task.TaskExecutor;
//import com.biuqu.task.impl.CsvBatchExecutor;
//import com.biuqu.task.impl.CsvExecutor;
//import com.biuqu.task.impl.SumAsyncExecutor;
//import com.biuqu.task.queue.AsyncBatchQueue;
//import com.biuqu.task.queue.AsyncConfig;
//import com.biuqu.thread.CommonThreadPool;
//import org.assertj.core.util.Lists;
//import org.junit.Test;
//
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.TimeUnit;
//
///**
// * 异步测试
// *
// * @author BiuQu
// * @date 2023/5/4 11:21
// */
//public class ExecutorTest
//{
//    @Test
//    public void asyncTest()
//    {
//
//        String basePath = this.getClass().getResource("/").getPath();
//        RandomAccessFileReader reader = new RandomAccessFileReader(basePath + "file/random_access.csv");
//        int size = 100;
//        int start = 0;
//        try
//        {
//            int i = 0;
//            List<String> batchData = null;
//            List<Csv> allData = Lists.newArrayList();
//            while ((batchData = reader.readLines(start, size)) != null && batchData.size() > 0)
//            {
//                List<Csv> batchCsv = Csv.build(batchData);
//                System.out.println("[" + (++i) + "]read csv:" + batchData);
//                allData.addAll(batchCsv);
//                start += size;
//            }
//
//            ExecutorService threadPool = CommonThreadPool.getExecutor("batch", 20, 20);
//            TaskExecutor<Csv> taskExecutor = new CsvExecutor();
//            BaseBatchAsyncExecutor<Csv> baseExecutor = new CsvBatchExecutor(threadPool, taskExecutor, 5 * 60, true);
//            SumAsyncExecutor<Csv> sumAsyncExecutor = new SumAsyncExecutor<>(baseExecutor, 3, 10, 10 * 60);
//            sumAsyncExecutor.execute(allData);
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void asyncTest2()
//    {
//
//        String basePath = this.getClass().getResource("/").getPath();
//        RandomAccessFileReader reader = new RandomAccessFileReader(basePath + "file/random_access.csv");
//        int size = 100;
//        int start = 0;
//        try
//        {
//            ExecutorService threadPool = CommonThreadPool.getExecutor("batch", 20, 20);
//            TaskExecutor<Csv> taskExecutor = new CsvExecutor();
//            BaseBatchAsyncExecutor<Csv> baseExecutor = new CsvBatchExecutor(threadPool, taskExecutor, 5 * 60, true);
//            SumAsyncExecutor<Csv> sumAsyncExecutor = new SumAsyncExecutor<>(baseExecutor, 3, 10, 10 * 60);
//
//            int i = 0;
//            List<String> batchData = null;
//            List<Csv> allData = Lists.newArrayList();
//            while ((batchData = reader.readLines(start, size)) != null && batchData.size() > 0)
//            {
//                List<Csv> batchCsv = Csv.build(batchData);
//                System.out.println("[" + (++i) + "]read csv:" + batchData);
//                allData.addAll(batchCsv);
//                start += size;
//
//                sumAsyncExecutor.execute(batchCsv);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void csvTest()
//    {
//
//        String basePath = this.getClass().getResource("/").getPath();
//        RandomAccessFileReader reader = new RandomAccessFileReader(basePath + "file/random_access.csv");
//        int size = 1000;
//        int start = 0;
//
//        try
//        {
//            int i = 0;
//            List<String> batchData = null;
//            List<Csv> allData = Lists.newArrayList();
//            while ((batchData = reader.readLines(start, size)) != null && batchData.size() > 0)
//            {
//                List<Csv> batchCsv = Csv.build(batchData);
//                System.out.println("[" + (++i) + "]read csv:" + batchData);
//                allData.addAll(batchCsv);
//                start += size;
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void csvTest2() throws InterruptedException
//    {
//        String basePath = this.getClass().getResource("/").getPath();
//        String csvPath = basePath + "file/random_access.csv";
//        AsyncBatchQueue<Csv> csvQueue = new AsyncBatchQueue<>(5000);
//
//        AsyncConfig consumer = new AsyncConfig(AsyncConfig.Type.Consumer, "csv1", 200, 5);
//        AsyncConfig producer = new AsyncConfig(AsyncConfig.Type.Producer, "csv1", 100, 12);
//        CsvBatchWriter csvWriter = new CsvBatchWriter(csvQueue, csvPath, producer);
//        CsvBatchReader csvReader = new CsvBatchReader(csvQueue, consumer);
//        csvReader.read();
//        int total = csvWriter.write();
//
//        long start = System.currentTimeMillis();
//        int count = csvReader.getCount();
//
//        long expire = TimeUnit.SECONDS.toMillis(10);
//        while (count < total)
//        {
//            Thread.sleep(200l);
//            //读取的数据量有变化，说明还有在读取数据池中的数据
//            if (count != csvReader.getCount())
//            {
//                start = System.currentTimeMillis();
//            }
//            else if (System.currentTimeMillis() - start >= expire)
//            {
//                System.out.println("quit now.read csv num:" + total + ",write num:" + csvReader.getCount());
//                csvWriter.shutdown();
//                csvReader.shutdown();
//                csvQueue.release();
//                break;
//            }
//            count = csvReader.getCount();
//        }
//        csvWriter.shutdown();
//        csvReader.shutdown();
//        System.out.println("successfully quit now.read csv num:" + total + ",write num:" + csvReader.getCount());
//    }
//
//    @Test
//    public void csvTest3()
//    {
//        AsyncConfig consumer = new AsyncConfig(AsyncConfig.Type.Consumer, "csv1", 100, 5);
//        AsyncConfig producer = new AsyncConfig(AsyncConfig.Type.Producer, "csv1", 200, 2);
//        CsvAsyncExecutor executor = new CsvAsyncExecutor(consumer, producer, 500);
//
//        String basePath = this.getClass().getResource("/").getPath();
//        String csvPath = basePath + "file/random_access.csv";
//        boolean result = executor.execute(csvPath);
//        System.out.println("read file:" + csvPath + ",result:" + result);
//    }
//}