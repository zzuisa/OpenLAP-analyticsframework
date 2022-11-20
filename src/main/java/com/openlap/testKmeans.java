package com.openlap;
import com.openlap.dataset.Os;
import weka.clusterers.SimpleKMeans; //导入依赖的package包/类
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
/**
 Created By Ao on 2022/9/1
 */
public class testKmeans {

        Instances cpu = null;
        SimpleKMeans kmeans;

        public void loadArff(String arffInput){
            DataSource source = null;
            try {
                source = new DataSource(arffInput);
                cpu = source.getDataSet();
            } catch (Exception e1) {
            }
        }

        public void clusterData(){
            kmeans = new SimpleKMeans();
            kmeans.setSeed(10);
            try {
                kmeans.setPreserveInstancesOrder(true);
                kmeans.setNumClusters(10);
                kmeans.buildClusterer(cpu);
                int[] assignments = kmeans.getAssignments();
                int i = 0;
                for(int clusterNum : assignments) {
                    System.out.printf("Instance %d -> Cluster %d\n", i, clusterNum);
                    i++;
                }
            } catch (Exception e1) {
            }
        }

        public static void main(String[] args) throws Exception{
            testKmeans test = new testKmeans();
            test.loadArff("C:\\Program Files\\Weka-3-6\\data\\cpu.arff");
            test.clusterData();
        }

}
