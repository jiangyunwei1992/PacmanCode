package edu.neu.coe.core;

import java.io.*;
import java.util.*;
/**
 * 
 * @author jiangyunwei
 *	还有一点小问题
 */
class SVMModel
{
	int preferedLabel;
	int otherLabel;
	double[] weight;
	double bias;
	public SVMModel(int preferedLabel,int otherLabel,double[] weight,double bias)
	{
		this.preferedLabel = preferedLabel;
		this.otherLabel = otherLabel;
		this.weight = weight;
		this.bias = bias;
	}
}
public class SVMAlgorithm {
	private static int numPeople=0;
	private static double[] transformedLabel;
	private static int selectJRandomly(int i,int m)
	{
		Random rand = new Random();
		int j=i;
		while(j==i)
			j = rand.nextInt(m);
		return j;
	}
	private static double clipAlpha(double alpha_j,double H,double L)
	{
		if(alpha_j>H) alpha_j = H;
		else if(alpha_j<L) alpha_j = L;
		return alpha_j;
	}
	public static void transform(int[] trainY,int target)
	{
		transformedLabel = new double[trainY.length];
		for(int i=0;i<trainY.length;i++)
		{
			if(trainY[i]!=target)
			{
				transformedLabel[i] = -1;
			}else
			{
				transformedLabel[i] = 1;
			}
		}
	}
	
	public static void SVM(double[][] trainX,int[] trainY,double C,double toler,int maxIteration,int target,int compared,double gamma)
	{
		String path = System.getProperty("user.dir")+"/src/edu/neu/coe/resources/models/svm/weight"+target+"_"+compared+".txt";
		transform(trainY,target);// train a model for each class
		//System.out.println(Arrays.toString(transformedLabel));
		double b = 0;
		int m = trainX.length;
		int n = trainX[0].length;
		int iteration = 0;
		double[] alphas = new double[m];
		while(iteration<maxIteration)
		{
			int alphaPairsChanged = 0;// if no pairs change, continue
			for(int i=0;i<m;i++)
			{
				//fXi = (alphas*Y)·(K(X,X[i]))+b
				double fXi = Utils.vectorTimesVector(Utils.vectorMulVector(alphas, transformedLabel), Utils.kernelRBF(trainX,trainX[i],gamma))+b;
				// calculate errors
				double Ei = fXi - transformedLabel[i];
				//System.out.println(Ei);
				if(transformedLabel[i]*Ei<-toler&&alphas[i]<C||transformedLabel[i]*Ei>toler&&alphas[i]>0)
				{
					int j = selectJRandomly(i,m);
					double fXj = Utils.vectorTimesVector(Utils.vectorMulVector(alphas, transformedLabel), Utils.kernelRBF(trainX,trainX[j],gamma))+b;
					double Ej = fXj - transformedLabel[j];
					//update alpha
					double alphaIOld = alphas[i];
					double alphaJOld = alphas[j];
					double L = 0, H = 0;
					/**
					 * 	According to the SMO algorithm
					 *  if Y[i] != Y[j]
					 *  		L = max(0,alphas[j]-alphas[i])
					 *  		H = min(C,C+alphas[j]-alphas[i])
					 *  else 
					 *  		L = max(0,alphas[j]+alphas[i]-C)
					 *  		H = min(C,alphas[j]+alphas[i])
					 */
					if(transformedLabel[i]!=transformedLabel[j])
					{
						L = Math.max(0, alphas[j]-alphas[i]);
						H = Math.min(C, C+alphas[j]-alphas[i]);
					}else
					{
						L = Math.max(0, alphas[j]+alphas[i]-C);
						H = Math.min(C, alphas[j]+alphas[i]);
					}
					// if L = H, go to next iteration;otherwise, update eta and alphas
					if(L==H) continue;
//					double eta = 2*Utils.vectorTimesVector(Utils.kernelRBF(trainX[i], gamma), Utils.kernelRBF(trainX[j], gamma))-Utils.vectorTimesVector(Utils.kernelRBF(trainX[i], gamma), Utils.kernelRBF(trainX[i], gamma)) - Utils.vectorTimesVector(Utils.kernelRBF(trainX[j], gamma), Utils.kernelRBF(trainX[j], gamma));
					double eta = 2*Utils.kernelRBF(trainX[i], trainX[j], gamma)-Utils.kernelRBF(trainX[i], trainX[i], gamma)-Utils.kernelRBF(trainX[j], trainX[j], gamma);
					
					System.out.println("eta="+eta);
					if(eta>=0) continue;//eta should be negative
					alphas[j] -=transformedLabel[j]*(Ei-Ej)/eta;
					alphas[j] = clipAlpha(alphas[j],H,L);
					if(Math.abs(alphas[j]-alphaJOld)<0.00001) continue;// ignore small changes
					alphas[i] +=transformedLabel[j]*transformedLabel[i]*(alphaJOld-alphas[j]);
//					double b1 = b-Ei-transformedLabel[i]*(alphas[i]-alphaIOld)
//							*(Utils.vectorTimesVector(Utils.kernelRBF(trainX[i], gamma),Utils.kernelRBF(trainX[i], gamma)))
//							-transformedLabel[j]*(alphas[j]-alphaJOld)*Utils.vectorTimesVector(Utils.kernelRBF(trainX[i], gamma),Utils.kernelRBF(trainX[j], gamma));
					double b1 = b-Ei-transformedLabel[i]*(alphas[i]-alphaIOld)
								*Utils.kernelRBF(trainX[i], trainX[i], gamma)
								-transformedLabel[j]*(alphas[j]-alphaJOld)
								*Utils.kernelRBF(trainX[i], trainX[j], gamma);
//					double b2 = b-Ej-transformedLabel[i]*(alphas[i]-alphaIOld)
					//*(Utils.vectorTimesVector(Utils.kernelRBF(trainX[i], gamma),Utils.kernelRBF(trainX[j], gamma)))
					//-transformedLabel[j]*(alphas[j]-alphaJOld)
					//*Utils.vectorTimesVector(Utils.kernelRBF(trainX[j], gamma),Utils.kernelRBF(trainX[j], gamma));
					double b2 = b-Ej-transformedLabel[i]*(alphas[i]-alphaIOld)
							*Utils.kernelRBF(trainX[i], trainX[i], gamma)
							-transformedLabel[j]*(alphas[j]-alphaJOld)
							*Utils.kernelRBF(trainX[j], trainX[j], gamma);
					System.out.println(b1+"--"+b2);
					if(0<alphas[i]&&alphas[i]<C) b=b1;
					else if(0<alphas[j]&&alphas[j]<C) b=b2;
					else b=(b1+b2)/2.0;
					alphaPairsChanged+=1;
				}
			}
			if(alphaPairsChanged==0) iteration+=1;
			else iteration = 0;
		}
//		double[] weight = calculateWeights(alphas,transformedLabel,trainX);
//		
//		Utils.save(weight, b, path);
		//System.out.println(Arrays.toString(alphas));
		Utils.save(alphas, b,path);
	}
	public static void SVM(int num,double C,double toler,int maxIteration,double gamma)
	{
		File trainXFile = new File(System.getProperty("user.dir")+"/src/edu/neu/coe/resources/text/trainX.txt");
		File trainYFile = new File(System.getProperty("user.dir")+"/src/edu/neu/coe/resources/text/trainY.txt");
		
		double[][] trainX = Utils.ReadFile(trainXFile);
		int[] trainY = Utils.readFile(trainYFile);
		//System.out.println(Arrays.toString(trainY));
		// divide into groups
		List<double[][]> groups = new ArrayList<double[][]>();
		List<int[]> labelGroups = new ArrayList<int[]>();
		int index = 0;
		while(index<num)
		{
			List<double[]> group = new ArrayList<double[]>();
			List<Integer> labelGroup = new ArrayList<Integer>();
			for(int i=0;i<5;i++)
			{
				group.add(trainX[index*5+i]);
				labelGroup.add(trainY[index*5+i]);
			}
			//System.out.println(labelGroup);
			double[][] groupArray = new double[group.size()][group.get(0).length];
			int[] labelGroupArray = new int[labelGroup.size()];
			for(int i=0;i<group.size();i++)
			{
				groupArray[i] = group.get(i);
				labelGroupArray[i] = labelGroup.get(i);
			}
			groups.add(groupArray);
			labelGroups.add(labelGroupArray);
			index++;
		}
		numPeople = num;

		int cnt = 0;
		for(int i=0;i<groups.size();i++)
		{
			for(int j=i+1;j<groups.size();j++)
			{
				//cnt++;
				double[][] groupI = groups.get(i);
				double[][] groupJ = groups.get(j);
				double[][] combinedGroup = Utils.combineTwo2DArray(groupI, groupJ);
				int[] labelGroupI = labelGroups.get(i);
				int[] labelGroupJ = labelGroups.get(j);
				int[] combinedLabels = Utils.combineTwoArray(labelGroupI, labelGroupJ);
				int target = labelGroupI[0];
				int compared =labelGroupJ[0];
				SVM(combinedGroup,combinedLabels,C,toler,maxIteration,target,compared,gamma);
			}
		}
		//System.out.println(cnt);
	}

	public static int votePrediction(Map<Integer,Integer> cnt)
	{
		int vote = -1;
		int max = 0;
		Iterator<Integer> it = cnt.keySet().iterator();
		while(it.hasNext())
		{
			int key = it.next();
			int value = cnt.get(key);
			if(value>max)
			{
				vote = key;
				max = value;
			}
		}
		return vote;
	}
	public static void SVMTest(double gamma)
	{
		File testXFile = new File(System.getProperty("user.dir")+"/src/edu/neu/coe/resources/text/testX.txt");
		File testYFile = new File(System.getProperty("user.dir")+"/src/edu/neu/coe/resources/text/testY.txt");
		double[][] testX = Utils.ReadFile(testXFile);
		int[] testY = Utils.readFile(testYFile);
		//System.out.println(Arrays.toString(testY));
		List<List<Integer>> positive = new ArrayList<List<Integer>>();
		List<SVMModel> models = new ArrayList<SVMModel>();
		String modelDirectory = System.getProperty("user.dir")+"/src/edu/neu/coe/resources/models/svm";
		File directoryFile = new File(modelDirectory);
		File[] files = directoryFile.listFiles();
		for(File file:files)
		{
			models.add(Utils.readModel(file));
		}
		int errorCnt = 0;
		int xxx = -1;
		for(int i=0;i<testX.length;i++)
		{
			double[] x = testX[i];
			Map<Integer,Integer> cnt = new TreeMap<Integer,Integer>();
			for(SVMModel model:models)
			{
				double[] weight = model.weight;
				//System.out.println(Arrays.toString(weight));
				double sum = model.bias + Utils.vectorTimesVector(weight, Utils.kernelRBF(x, gamma));
				int predict = sum>0?model.preferedLabel:model.otherLabel;
				
				if(!cnt.containsKey(predict))
				{
					cnt.put(predict, 1);
				}else
				{
					int newValue = cnt.get(predict)+1;
					cnt.replace(predict, newValue);
				}
			}

//			int vote = votePrediction(cnt);
//			int actual = testY[i];
//			if(vote!=actual) errorCnt++;
//			System.out.println("actual class is "+actual+", prediction is "+vote);
		}
		System.out.println(errorCnt);
		
	}
}
