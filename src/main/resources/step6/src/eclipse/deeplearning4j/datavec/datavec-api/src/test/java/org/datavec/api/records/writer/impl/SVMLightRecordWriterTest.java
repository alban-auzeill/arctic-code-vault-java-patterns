/******************************************************************************* (rank 47) copied from https://github.com/eclipse/deeplearning4j/blob/881a672fa13f3b37177eb2ea023e39b0de893645/datavec/datavec-api/src/test/java/org/datavec/api/records/writer/impl/SVMLightRecordWriterTest.java
 * Copyright (c) 2015-2018 Skymind, Inc.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.datavec.api.records.writer.impl;

import org.apache.commons.io.FileUtils;
import org.datavec.api.conf.Configuration;
import org.datavec.api.records.reader.impl.misc.SVMLightRecordReader;
import org.datavec.api.records.writer.impl.misc.SVMLightRecordWriter;
import org.datavec.api.split.FileSplit;
import org.datavec.api.split.partition.NumberOfRecordsPartitioner;
import org.datavec.api.writable.*;
import org.junit.Test;
import org.nd4j.common.tests.BaseND4JTest;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.common.io.ClassPathResource;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for SVMLightRecordWriter. Replaces writer tests in
 * SVMRecordWriterTest.
 *
 * @see SVMLightRecordReader
 * @see org.datavec.api.records.reader.impl.SVMRecordWriterTest
 *
 * @author dave@skymind.io
 */
public class SVMLightRecordWriterTest extends BaseND4JTest {

    @Test
    public void testBasic() throws Exception {
        Configuration configWriter = new Configuration();

        Configuration configReader = new Configuration();
        configReader.setInt(SVMLightRecordReader.NUM_FEATURES, 10);
        configReader.setBoolean(SVMLightRecordReader.ZERO_BASED_INDEXING, false);

        File inputFile = new ClassPathResource("datavec-api/svmlight/basic.txt").getFile();
        executeTest(configWriter, configReader, inputFile);
    }

    @Test
    public void testNoLabel() throws Exception {
        Configuration configWriter = new Configuration();
        configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
        configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 9);

        Configuration configReader = new Configuration();
        configReader.setInt(SVMLightRecordReader.NUM_FEATURES, 10);
        configReader.setBoolean(SVMLightRecordReader.ZERO_BASED_INDEXING, false);

        File inputFile = new ClassPathResource("datavec-api/svmlight/noLabels.txt").getFile();
        executeTest(configWriter, configReader, inputFile);
    }

    @Test
    public void testMultioutputRecord() throws Exception {
        Configuration configWriter = new Configuration();
        configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
        configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 9);

        Configuration configReader = new Configuration();
        configReader.setInt(SVMLightRecordReader.NUM_FEATURES, 10);
        configReader.setBoolean(SVMLightRecordReader.ZERO_BASED_INDEXING, false);

        File inputFile = new ClassPathResource("datavec-api/svmlight/multioutput.txt").getFile();
        executeTest(configWriter, configReader, inputFile);
    }

    @Test
    public void testMultilabelRecord() throws Exception {
        Configuration configWriter = new Configuration();
        configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
        configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 9);
        configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);

        Configuration configReader = new Configuration();
        configReader.setInt(SVMLightRecordReader.NUM_FEATURES, 10);
        configReader.setBoolean(SVMLightRecordReader.MULTILABEL, true);
        configReader.setInt(SVMLightRecordReader.NUM_LABELS, 4);
        configReader.setBoolean(SVMLightRecordReader.ZERO_BASED_INDEXING, false);

        File inputFile = new ClassPathResource("datavec-api/svmlight/multilabel.txt").getFile();
        executeTest(configWriter, configReader, inputFile);
    }

    @Test
    public void testZeroBasedIndexing() throws Exception {
        Configuration configWriter = new Configuration();
        configWriter.setBoolean(SVMLightRecordWriter.ZERO_BASED_INDEXING, true);
        configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
        configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 10);
        configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);

        Configuration configReader = new Configuration();
        configReader.setInt(SVMLightRecordReader.NUM_FEATURES, 11);
        configReader.setBoolean(SVMLightRecordReader.MULTILABEL, true);
        configReader.setInt(SVMLightRecordReader.NUM_LABELS, 5);

        File inputFile = new ClassPathResource("datavec-api/svmlight/multilabel.txt").getFile();
        executeTest(configWriter, configReader, inputFile);
    }

    public static void executeTest(Configuration configWriter, Configuration configReader, File inputFile) throws Exception {
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            SVMLightRecordReader rr = new SVMLightRecordReader();
            rr.initialize(configReader, new FileSplit(inputFile));
            while (rr.hasNext()) {
                List<Writable> record = rr.next();
                writer.write(record);
            }
        }

        Pattern p = Pattern.compile(String.format("%s:\\d+ ", SVMLightRecordReader.QID_PREFIX));
        List<String> linesOriginal = new ArrayList<>();
        for (String line : FileUtils.readLines(inputFile)) {
            if (!line.startsWith(SVMLightRecordReader.COMMENT_CHAR)) {
                String lineClean = line.split(SVMLightRecordReader.COMMENT_CHAR, 2)[0];
                if (lineClean.startsWith(" ")) {
                    lineClean = " " + lineClean.trim();
                } else {
                    lineClean = lineClean.trim();
                }
                Matcher m = p.matcher(lineClean);
                lineClean = m.replaceAll("");
                linesOriginal.add(lineClean);
            }
        }
        List<String> linesNew = FileUtils.readLines(tempFile);
        assertEquals(linesOriginal, linesNew);
    }

    @Test
    public void testNDArrayWritables() throws Exception {
        INDArray arr2 = Nd4j.zeros(2);
        arr2.putScalar(0, 11);
        arr2.putScalar(1, 12);
        INDArray arr3 = Nd4j.zeros(3);
        arr3.putScalar(0, 13);
        arr3.putScalar(1, 14);
        arr3.putScalar(2, 15);
        List<Writable> record = Arrays.asList((Writable) new DoubleWritable(1),
                                            new NDArrayWritable(arr2),
                                            new IntWritable(2),
                                            new DoubleWritable(3),
                                            new NDArrayWritable(arr3),
                                            new IntWritable(4));
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        String lineOriginal = "13.0,14.0,15.0,4 1:1.0 2:11.0 3:12.0 4:2.0 5:3.0";

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            Configuration configWriter = new Configuration();
            configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 3);
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            writer.write(record);
        }

        String lineNew = FileUtils.readFileToString(tempFile).trim();
        assertEquals(lineOriginal, lineNew);
    }

    @Test
    public void testNDArrayWritablesMultilabel() throws Exception {
        INDArray arr2 = Nd4j.zeros(2);
        arr2.putScalar(0, 11);
        arr2.putScalar(1, 12);
        INDArray arr3 = Nd4j.zeros(3);
        arr3.putScalar(0, 0);
        arr3.putScalar(1, 1);
        arr3.putScalar(2, 0);
        List<Writable> record = Arrays.asList((Writable) new DoubleWritable(1),
                new NDArrayWritable(arr2),
                new IntWritable(2),
                new DoubleWritable(3),
                new NDArrayWritable(arr3),
                new DoubleWritable(1));
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        String lineOriginal = "2,4 1:1.0 2:11.0 3:12.0 4:2.0 5:3.0";

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            Configuration configWriter = new Configuration();
            configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 3);
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            writer.write(record);
        }

        String lineNew = FileUtils.readFileToString(tempFile).trim();
        assertEquals(lineOriginal, lineNew);
    }

    @Test
    public void testNDArrayWritablesZeroIndex() throws Exception {
        INDArray arr2 = Nd4j.zeros(2);
        arr2.putScalar(0, 11);
        arr2.putScalar(1, 12);
        INDArray arr3 = Nd4j.zeros(3);
        arr3.putScalar(0, 0);
        arr3.putScalar(1, 1);
        arr3.putScalar(2, 0);
        List<Writable> record = Arrays.asList((Writable) new DoubleWritable(1),
                new NDArrayWritable(arr2),
                new IntWritable(2),
                new DoubleWritable(3),
                new NDArrayWritable(arr3),
                new DoubleWritable(1));
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        String lineOriginal = "1,3 0:1.0 1:11.0 2:12.0 3:2.0 4:3.0";

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            Configuration configWriter = new Configuration();
            configWriter.setBoolean(SVMLightRecordWriter.ZERO_BASED_INDEXING, true); // NOT STANDARD!
            configWriter.setBoolean(SVMLightRecordWriter.ZERO_BASED_LABEL_INDEXING, true); // NOT STANDARD!
            configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 3);
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            writer.write(record);
        }

        String lineNew = FileUtils.readFileToString(tempFile).trim();
        assertEquals(lineOriginal, lineNew);
    }

    @Test
    public void testNonIntegerButValidMultilabel() throws Exception {
        List<Writable> record = Arrays.asList((Writable) new IntWritable(3),
                new IntWritable(2),
                new DoubleWritable(1.0));
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            Configuration configWriter = new Configuration();
            configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 1);
            configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            writer.write(record);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void nonIntegerMultilabel() throws Exception {
        List<Writable> record = Arrays.asList((Writable) new IntWritable(3),
                                                new IntWritable(2),
                                                new DoubleWritable(1.2));
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            Configuration configWriter = new Configuration();
            configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 1);
            configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            writer.write(record);
        }
    }

    @Test(expected = NumberFormatException.class)
    public void nonBinaryMultilabel() throws Exception {
        List<Writable> record = Arrays.asList((Writable) new IntWritable(0),
                new IntWritable(1),
                new IntWritable(2));
        File tempFile = File.createTempFile("SVMLightRecordWriter", ".txt");
        tempFile.setWritable(true);
        tempFile.deleteOnExit();
        if (tempFile.exists())
            tempFile.delete();

        try (SVMLightRecordWriter writer = new SVMLightRecordWriter()) {
            Configuration configWriter = new Configuration();
            configWriter.setInt(SVMLightRecordWriter.FEATURE_FIRST_COLUMN, 0);
            configWriter.setInt(SVMLightRecordWriter.FEATURE_LAST_COLUMN, 1);
            configWriter.setBoolean(SVMLightRecordWriter.MULTILABEL, true);
            FileSplit outputSplit = new FileSplit(tempFile);
            writer.initialize(configWriter,outputSplit,new NumberOfRecordsPartitioner());
            writer.write(record);
        }
    }
}
