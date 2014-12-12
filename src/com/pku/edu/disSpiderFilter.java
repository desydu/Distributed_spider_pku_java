package pku.edu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.ByteArrayInputStream;
import java.util.HashSet;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class disSpiderFilter {

    public static class TokenizerMapper
            extends Mapper<Object, Text, Text, IntWritable> {

        private final static IntWritable one = new IntWritable(1);
        private Text word = new Text();
        HashSet<String> ruleswords = new HashSet<String>();

        private void parseRulesWords(String path) {

            try {
                String word = null;
                BufferedReader reader = new BufferedReader(new FileReader(path));
                while ((word = reader.readLine()) != null) {
                    ruleswords.add(word);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }

        @SuppressWarnings("deprecation")
        public void setup(Context context) {
            Path[] patternsFiles = new Path[0];
            try {
                patternsFiles = DistributedCache.getLocalCacheFiles(context.getConfiguration());
                System.out.println("success read");
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (patternsFiles == null) {
                System.out.println("have no stopfile\n");
                return;
            }

            //read stop-words into HashSet  
            for (Path patternsFile : patternsFiles) {
                parseRulesWords(patternsFile.toString());
            }
        }

        public void map(Object key, Text value, Context context
        ) throws IOException, InterruptedException {

            byte[] bt = value.getBytes();
            InputStream ip = new ByteArrayInputStream(bt);
            Reader read = new InputStreamReader(ip);
            IKSegmenter iks = new IKSegmenter(read, true);
            Lexeme t;
            while ((t = iks.next()) != null) {
                if (ruleswords.contains(t.getLexemeText())) {
                    word.set(t.getLexemeText());
                    context.write(word, one);
                }

            }
        }
    }

    public static class IntSumReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {
        private IntWritable result = new IntWritable();

        public void reduce(Text key, Iterable<IntWritable> values,
                           Context context
        ) throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        String[] otherArgs = new GenericOptionsParser(conf, args).getRemainingArgs();
        if (otherArgs.length != 4) {
            System.err.println("Usage: wordcount <in> <out> -rule <rule>");
            System.exit(2);
        }

        for (int i = 0; i < args.length; i++) {
            if ("-rule".equals(args[i])) {
                DistributedCache.addCacheFile(new Path(args[++i]).toUri(), conf);
                System.out.println("======================================================");
                System.out.println("#####################success!! involked!!########################");
                System.out.println("======================================================");
            }
        }

        Job job = new Job(conf, "word count");
        job.setJarByClass(disSpiderFilter.class);
        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));
        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}