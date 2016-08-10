package edu.neu.coe.core;

import java.io.*;
import java.util.*;
public class Utils 
{
	public static double[] vectorAddVector(double[] vec1,double[] vec2)
	{
		double[] result = new double[vec1.length];
		for(int i=0;i<vec1.length;i++)
		{
			result[i] = vec1[i]+vec2[i];
		}
		return result;
	}
	public static double[] constantMulVector(double constant,double[] vec)
	{
		double[] result = new double[vec.length];
		for(int i=0;i<vec.length;i++)
			result[i] = vec[i]*constant;
		return result;
	}
	/**
	 * 
	 * @param vec
	 * @return average value of all elements of the vector
	 */
	public static double vectorMean(double[] vec)
	{
		double sum = 0;
		for(double elem:vec)
		{
			sum+= elem;
		}
		return sum/vec.length;
	}
	/**
	 * 
	 * @param vec
	 * @return variance of a vector
	 */
	public static double vectorVar(double[] vec)
	{
		double mean = vectorMean(vec);
		double sum = 0;
		for(double elem:vec)
		{
			sum += Math.pow(elem-mean, 2);
		}
		return sum/(vec.length-1);
	}
	/**
	 * 
	 * @param vec
	 * @return normalized vector
	 */
	public static double[] normalizeVector(double[] vec)
	{
		double[] result = vec.clone();
		double mean = vectorMean(vec);
		double var = vectorVar(vec);
		for(int i=0;i<vec.length;i++)
		{
			result[i] = 256*(vec[i]-mean)/var;
		}
		return result;
	}
	/**
	 * 
	 * @param vecA
	 * @param vecB
	 * @return the differences between 2 vectors
	 */
	public static double[] vectorSubVector(double[] vecA,double[] vecB)
	{
		double[] result = new double[vecA.length];
		for(int i=0;i<vecA.length;i++)
			result[i] = vecA[i] - vecB[i];
		return result;
	}
	/**
	 * 
	 * @param vec
	 * @return the sum of all elements of an vector
	 */
	public static double sum(double[] vec)
	{
		double sum = 0;
		for(double elem:vec)
			sum+= elem;
		return sum;
	}
	/**
	 * 
	 * @param vec
	 * @param start
	 * @param end
	 * @return the sum of elements between start and end
	 */
	public static double sum(double[] vec,int start,int end)
	{
		double sum = 0;
		for(int i = start; i <=end;i++)
			sum+=vec[i];
		return sum;
	}
	/**
	 * 
	 * @param matrix
	 * @return transform 2-D array to 1-D array
	 */
	public static double[] matrix2Vector(double[][] matrix)
	{
		if(matrix==null) return null;
		int height = matrix.length;
		int width = matrix[0].length;
		int vecSize = height*width;
		double[] vector = new double[vecSize];
		int index = 0;
		// extract columns first and then rows, left to right
		for(int i=0;i<width;i++)
		{
			for(int j=0;j<height;j++)
			{
				vector[index++] = matrix[j][i];
			}
		}
		return vector;
	}
	
	/**
	 * 
	 * @param allCoor
	 * @return the normalized data
	 */
	public static double[][] unitNormalize(double[][] allCoor)
	{
		double[][] x = allCoor.clone();
		int m = x.length;
		int n = x[0].length;
		double[][] S = new double[m][n];
		for(int i=0;i<m;i++)
		{
			S[i] = normalizeVector(x[i]);
		}
		return S;
	}
	/**
	 * 
	 * @param array
	 * @param path
	 */
	public static void save(double[][] array,String path)
	{
		File file = new File(path);
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			for(int i = 0;i<array.length;i++)
			{
				for(int j=0;j<array[0].length;j++)
				{
					writer.write(array[i][j]+" ");
				}
				writer.write("\n");
			}
			writer.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(writer!=null) writer.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static void save(int[] array,String path)
	{
		File file = new File(path);
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			for(int i = 0;i<array.length;i++)
			{
				writer.write(array[i]+" ");
				writer.write("\n");
			}
			writer.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(writer!=null) writer.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static void save(double[] array,double b,String path)
	{
		File file = new File(path);
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			for(int i = 0;i<array.length;i++)
			{
				writer.write(array[i]+" ");
			}
			writer.write("\n");
			writer.write(String.valueOf(b));
			writer.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(writer!=null) writer.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static void save(double[] array,String path)
	{
		File file = new File(path);
		FileWriter writer = null;
		try
		{
			writer = new FileWriter(file);
			for(int i = 0;i<array.length;i++)
			{
				writer.write(array[i]+" ");
			}
			writer.write("\n");
			writer.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(writer!=null) writer.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public static double[][] ReadFile(File file)
	{
		List<List<Double>> data = new ArrayList<List<Double>>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String line = null;
			while((line=br.readLine())!=null)
			{
				List<Double> row = new ArrayList<Double>();
				String[] words = line.split(" ");
				for(String word:words)
				{
					row.add(Double.parseDouble(word));
				}
				data.add(row);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(br!=null) br.close();
				if(isr!=null) isr.close();
				if(fis!=null) fis.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		double[][] result = new double[data.size()][data.get(0).size()];
		for(int i=0;i<data.size();i++)
		{
			for(int j=0;j<data.get(0).size();j++)
			{
				result[i][j] = data.get(i).get(j);
			}
		}
		//System.out.println(result.length+"---"+result[0].length);
		return result;
	}
	public static int[] readFile(File file)
	{
		List<Integer> list = new ArrayList<Integer>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String line = null;
			while((line=br.readLine())!=null)
			{
				String[] labels = line.split(" ");
				for(String label:labels)
				{
					list.add(Integer.parseInt(label));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(br!=null) br.close();
				if(isr!=null) isr.close();
				if(fis!=null) fis.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		int[] result = new int[list.size()];
		for(int i=0;i<list.size();i++)
			result[i] = list.get(i);
		return result;
	}
	public static double[] readFile2(File file)
	{
		List<Double> list = new ArrayList<Double>();
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String line = null;
			while((line=br.readLine())!=null)
			{
				String[] labels = line.split(" ");
				for(String label:labels)
				{
					list.add(Double.parseDouble(label));
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				if(br!=null) br.close();
				if(isr!=null) isr.close();
				if(fis!=null) fis.close();
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		double[] result = new double[list.size()];
		for(int i=0;i<list.size();i++)
			result[i] = list.get(i);
		return result;
	}
	public static double[] vectorMulVector(double[] v1,double[] v2)
	{
		double[] result = new double[v1.length];
		for(int i=0;i<v1.length;i++)
			result[i] = v1[i]*v2[i];
		return result;
	}
	public static double[] matrixMulVector(double[][] X,double[] V)
	{
		double[] vector = new double[X.length];
		for(int i=0;i<X.length;i++)
		{
			double sum = 0;
			for(int j=0;j<X[i].length;j++)
				sum+=X[i][j]*V[j];
			vector[i] = sum;
		}
		return vector;
	}
	public static double vectorTimesVector(double[] v1,double[] v2)
	{
		double sum = 0;
		for(int i=0;i<v1.length;i++)
		{
			sum+=v1[i]*v2[i];
		}
		return sum;
	}
	public static SVMModel readModel(File file)
	{
		String absolutePath = file.getAbsolutePath();
		int weightIndex = absolutePath.indexOf("weight")+"weight".length();
		int dotIndex = absolutePath.indexOf('.');
		String prefered_other = absolutePath.substring(weightIndex, dotIndex);
		//System.out.println(prefered_other);
		String[] labels = prefered_other.split("_");
		int preferedLabel = Integer.parseInt(labels[0]);
		int otherLabel = Integer.parseInt(labels[1]);
		//int label = Integer.parseInt();
		
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		List<List<Double>> model = new ArrayList<List<Double>>();
		try
		{
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String line = null;
			while((line=br.readLine())!=null)
			{
				String[] words = line.trim().split(" ");
				List<Double> element = new ArrayList<Double>();
				for(String word:words)
				{
					element.add(Double.parseDouble(word));
				}
				model.add(element);
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		double[] weight = new double[model.get(0).size()];
		for(int i=0;i<weight.length;i++)
		{
			weight[i] = model.get(0).get(i);
		}
		double bias = model.get(1).get(0);
		SVMModel svm = new SVMModel(preferedLabel,otherLabel,weight,bias);
		//System.out.println(svm.preferedLabel+"---"+svm.otherLabel);
		return svm;
		
	}
	public static double[] kernelRBF(double[] vec,double gamma)
	{
		//kernel = vec*vec'
		double[] kernel = new double[vec.length];
		for(int i=0;i<vec.length;i++)
		{
			kernel[i] = Math.exp(-gamma*Math.pow(vec[i]-vec[i], 2));
		}
		return kernel;
	}
	public static double[][] transpose(double[][] matrix)
	{
		double[][] result = new double[matrix[0].length][matrix.length];
		for(int i=0;i<matrix.length;i++)
		{
			for(int j=0;j<matrix[0].length;j++)
			{
				result[j][i] = matrix[i][j];
			}
		}
		return result;
	}
	public static double[][] kernelRBF(double[][] matrix,double gamma)
	{
		double[][] kernel = new double[matrix.length][];
		//System.out.println(matrix.length+"------"+matrix[0].length);
		double[][] transpose = Utils.transpose(matrix);
		
		//double[] mean = meanMatrix(matrix);
		for(int i=0;i<matrix.length;i++)
		{
			kernel[i] = kernelRBF(matrix[i],gamma);
		}
		return kernel;
	}
	public static double[][] combineTwo2DArray(double[][] array1,double[][] array2)
	{
		int idx = 0;
		int totalRecords = array1.length+array2.length;
		int totalFeatures = array1[0].length;
		double[][] result = new double[totalRecords][totalFeatures];
		for(int i=0;i<array1.length;i++)
		{
			result[idx++] = array1[i];
		}
		for(int i=0;i<array2.length;i++)
		{
			result[idx++] = array2[i];
		}
		return result;
	}
	public static int[] combineTwoArray(int[] array1,int[] array2)
	{
		int idx = 0;
		int totalRecords = array1.length+array2.length;
		int[] result = new int[totalRecords];
		for(int i=0;i<array1.length;i++)
		{
			result[idx++] = array1[i];
		}
		for(int i=0;i<array2.length;i++)
		{
			result[idx++] = array2[i];
		}
		return result;
	}
	public static double[] meanMatrix(double[][] matrix)
	{
		double[] mean = new double[matrix[0].length];
		int index = 0;
		for(int i=0;i<matrix.length;i++)
		{
			double sum=0;
			for(int j=0;j<matrix[0].length;j++)
			{
				sum+=matrix[i][j];
			}
			mean[index++] = sum/matrix[0].length;
		}
		return mean;
	}
//	public static double kernelRBF(double[] vec1,double[] vec2,double gamma)
//	{
//		double sum = 0;
//		for(int i=0;i<vec1.length;i++)
//			sum+=vec1[i]*vec2[i];
//		//System.out.println(sum);
//		return Math.exp(-gamma*Math.pow(sum, 2));
//	}
	public static double kernelRBF(double[] vec1,double[] vec2,double gamma)
	{
		double sum = 0;
		for(int i=0;i<vec1.length;i++)
		{
			sum+=Math.pow(vec1[i]-vec2[i], 2);
		}
		//exp(-gamma*|x-xc|^2)
		return Math.exp(-gamma*sum);
	}

	public static double[] kernelRBF(double[][] matrix,double[] vector,double gamma)
	{
		double[] result = new double[matrix.length];
		for(int i=0;i<matrix.length;i++)
			result[i] = kernelRBF(matrix[i],vector,gamma);
		return result;
	}
}
