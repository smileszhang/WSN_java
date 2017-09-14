package test1;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;

//import java.util.PriorityQueue;
//import java.util.Queue;
//import java.util.Collection;
//import java.math.BigInteger;
//import java.util.Comparator;

// The class representing the random geometric graph

public class RandomGeometricGraph {

	public Hashtable<Vertex, List<Vertex>> adjacencyList;
	private Hashtable<Integer, List<Vertex>> degreeList;
	private Random rand;
	private double r;
	public int type;
	public List<ArrayList<Vertex>> v00;
	public int maxindex = 0;

	public RandomGeometricGraph(double N, double degree, String graphtype) {
		v00 = new ArrayList<ArrayList<Vertex>>();
		adjacencyList = new Hashtable<Vertex, List<Vertex>>();
		rand = new Random();
		if (graphtype == "square") {
			this.r = Math.sqrt(degree / (N * Math.PI));
			newGraph(N, r, "square");
		} else if (graphtype == "disk") {
			this.r = Math.sqrt(degree / N);
			newGraph(N, r, "disk");
		} else if (graphtype == "sphere") {
			this.r = Math.sqrt(4 * degree / N);
			newGraph(N, r, "sphere");
		}
		type = 0;

	}

	public RandomGeometricGraph(Hashtable<Vertex, List<Vertex>> hash, double r) {
		adjacencyList = hash;
		this.r = r;
		type = 1;
	}

	public void setdegreeList(RandomGeometricGraph graph) {
		degreeList = new Hashtable<Integer, List<Vertex>>();
		int NUM = graph.getVertexWithMaxDegree(graph).degree;
		for (int i = NUM; i >= 0; i--) {
			degreeList.put(i, new ArrayList<Vertex>());
		}
		Iterator<Vertex> it = adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex u = it.next();
			degreeList.get(u.degree).add(u);
		}
		String str=new String();
		str="degree-number\r\n";
		for(int i:degreeList.keySet()){
			str+=String.valueOf(i)+","+String.valueOf(degreeList.get(i).size())+"\r\n";
		}
		writetxt(str,5);
	}

	public Hashtable<Integer, List<Vertex>> setchandegreeList(RandomGeometricGraph graph) {
		Hashtable<Integer, List<Vertex>> cdegreeList = new Hashtable<Integer, List<Vertex>>();
		int NUM = graph.getVertexWithMaxDegree2(graph).changedegree;
		for (int i = NUM; i >= 0; i--) {
			cdegreeList.put(i, new ArrayList<Vertex>());
		}
		Iterator<Vertex> it = adjacencyList.keySet().iterator();

		while (it.hasNext()) {
			Vertex u = it.next();
			cdegreeList.get(u.changedegree).add(u);
		}
		return cdegreeList;
	}

	// Return the distance r
	public double getThresholdDistance() {
		return r;
	}

	// Create a new graph with given number of vertices and distance
	// r
	public void newGraph(double numVertices, double r, String graphtype) {
		adjacencyList.clear();
		for (int i = 0; i < numVertices; i++) {
			Vertex v = createRandomVertex(graphtype);
			adjacencyList.put(v, new ArrayList<Vertex>());
		}

		Vertex[] vertices = getVertices(adjacencyList);
		for (int i = 0; i <= vertices.length - 1; i++) {
			Vertex v = vertices[i];
			for (int j = i + 1; j <= vertices.length - 1; j++) {
				Vertex u = vertices[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					adjacencyList.get(v).add(u);
					adjacencyList.get(u).add(v);
				}
			}
		}
		setVetxDeg();

	}

	// Return all the vertices of this graph
	public Vertex[] getVertices(Hashtable<Vertex, List<Vertex>> adjacencyList) {
		Vertex[] vertices = new Vertex[adjacencyList.size()];
		int i = 0;
		Iterator<Vertex> it = adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex v = it.next();
			vertices[i++] = v;
		}
		return vertices;
	}

	/**
	 * 
	 */
	public void smalllastordering(RandomGeometricGraph graphpp) {
		Hashtable<Vertex, List<Vertex>> copyadj = (Hashtable<Vertex, List<Vertex>>) adjacencyList.clone();
		RandomGeometricGraph graphcopy=new RandomGeometricGraph(copyadj,r);
		Hashtable<Vertex, List<Vertex>> adja = (Hashtable<Vertex, List<Vertex>>) adjacencyList.clone();
		int N=graphpp.getNumOfVertices(graphpp);
		int[] degreesize = new int[500];
		Iterator<Integer> it = degreeList.keySet().iterator();
		while (it.hasNext()) {
			int u = it.next();
			degreesize[u] = degreeList.get(u).size();
			// degreesize.add(degreeList.get(u).size());
		}

		List<List> colorOrder = new ArrayList();
		Vertex[] vertices = getVertices(copyadj);

		// vertices-degree plot before SLO
		String str = new String();
		str="degree before SLO\r\n";
		for (Vertex i : vertices) {
			str += i.getLabel() + "," + String.valueOf(i.degree) + "\r\n";
		}
		writetxt(str, 1);

		//SLO
		int NUM = getVertexWithMaxDegree(graphpp).changedegree;
		int min = 0;
		int flag=0;
		String strave=new String("average degree when deleted\r\n");
		while (!copyadj.isEmpty()) {
			for (int i = 0; i < NUM; i++) {
				if (degreeList.get(i).size() > 0) {
					min = i;
					break;
				}
			}
			//terminal clique
			if(flag==0){
				Vertex[] terminal_clique=getVertices(copyadj);
			    int ter=terminal_clique[0].changedegree;
			    int count=0;
			    for(Vertex i:terminal_clique){
			    	if(i.changedegree==ter){
			    		count++;
			    	}
			    }
			    if( count==terminal_clique.length){
			    	System.out.println("Terminal clique size: "+count);
			    	flag=1;
			    }
			    
			}	    
			strave+=String.valueOf(graphcopy.getAverageDegree(graphcopy))+"\r\n";
			
			if (min == 0) {
				List<Vertex> ready = new ArrayList();
				List<Vertex> list1 = degreeList.get(0);
				Vertex u = list1.remove(list1.size() - 1);
				copyadj.remove(u);
				ready.add(u);
				colorOrder.add(ready);
			} else {
				List<Vertex> list1 = degreeList.get(min);
				Vertex u = list1.remove(list1.size() - 1);
				List<Vertex> list2 = new ArrayList();
				list2 = copyadj.get(u);
				for (Vertex v : list2) {
					copyadj.get(v).remove(u);
					degreeList.get(v.changedegree).remove(v);
					v.changedegree--;
					degreeList.get(v.changedegree).add(v);
				}
				list2.add(0, u);
				colorOrder.add(list2);
				copyadj.remove(u);
			}
			

		}
		writetxt(strave,6);

		// vertices-degree plot after SLO
		String str1 = new String();
		str1="degree after SLO+\r\n";
		for (Vertex i : vertices) {
			str1 += i.getLabel() + "," + String.valueOf(i.changedegree) + "\r\n";
		}
		writetxt(str1, 2);

		// coloring
		Collections.reverse(colorOrder);
		List<Integer> count = new ArrayList();
		List<Integer> fix = new ArrayList();
		int counting = 1;
		List<Vertex> list0 = colorOrder.get(0);
		list0.get(0).color = 1;
		count.add(1);
		fix.add(1);
		for (int i = 1; i < colorOrder.size(); i++) {
			List<Vertex> list1 = new ArrayList();
			list1 = colorOrder.get(i);
			for (int j = 1; j < list1.size(); j++) {
				if (count.contains(list1.get(j).color)) {
					count.remove(list1.get(j).color);
				}
			}
			if (count.size() == 0) {
				counting++;
				fix.add(counting);
				list1.get(0).color = counting;
			} else {
				list1.get(0).color = count.get(count.size() - 1);
			}
			count.clear();
			for (Integer k : fix) {
				count.add(k);
			}
		}
		
		//print out number of colors
		System.out.println("number of colors: "+counting);
		
		int[] qq = new int[500];
		for (int i = 0; i < vertices.length; i++) {
			qq[vertices[i].color]++;
		}
		
		//print out color-number plot
		String strcolor=new String();
		strcolor="color,number\r\n";
		for(int i=1;i<=counting;i++){
			strcolor+=String.valueOf(i)+","+String.valueOf(qq[i])+"\r\n";
		}
		writetxt(strcolor,4);
		
		
		int[] maxnum = new int[4];
		int[] maxindex = new int[4];
		maxnum[0] = qq[0];
		maxindex[0] = 0;
		int p = 0;
		while (p < 4) {
			for (int i = 0; i < qq.length; i++) {
				if (qq[i] > maxnum[p]) {
					maxnum[p] = qq[i];
					maxindex[p] = i;
				}
			}
			
			qq[maxindex[p]] = -1;
			p++;
		}

		//print out max color set size
		System.out.println("maxcolor: "+maxindex[0]);
		System.out.println("maxcolorsize: "+maxnum[0]);
		
		
		// print out vertex's color
		String str2 = new String();
		str2="each vertex's color\r\n";
		for (Vertex i : vertices) {
			str2 += i.getLabel() + "," + String.valueOf(i.color) + "\r\n";
		}
		writetxt(str2, 3);

		Hashtable<Vertex, List<Vertex>> hash1 = new Hashtable<Vertex, List<Vertex>>();
		Hashtable<Vertex, List<Vertex>> hash2 = new Hashtable<Vertex, List<Vertex>>();
		Hashtable<Vertex, List<Vertex>> hash3 = new Hashtable<Vertex, List<Vertex>>();
		Hashtable<Vertex, List<Vertex>> hash4 = new Hashtable<Vertex, List<Vertex>>();
		Hashtable<Vertex, List<Vertex>> hash5 = new Hashtable<Vertex, List<Vertex>>();
		Hashtable<Vertex, List<Vertex>> hash6 = new Hashtable<Vertex, List<Vertex>>();

		for (int i = 0; i < vertices.length; i++) {
			if (vertices[i].color == maxindex[0] || vertices[i].color == maxindex[1]) {
				hash1.put(vertices[i], new ArrayList<Vertex>());
			}
			if (vertices[i].color == maxindex[0] || vertices[i].color == maxindex[2]) {
				hash2.put(vertices[i], new ArrayList<Vertex>());
			}
			if (vertices[i].color == maxindex[0] || vertices[i].color == maxindex[3]) {
				hash3.put(vertices[i], new ArrayList<Vertex>());
			}
			if (vertices[i].color == maxindex[1] || vertices[i].color == maxindex[2]) {
				hash4.put(vertices[i], new ArrayList<Vertex>());
			}
			if (vertices[i].color == maxindex[1] || vertices[i].color == maxindex[3]) {
				hash5.put(vertices[i], new ArrayList<Vertex>());
			}
			if (vertices[i].color == maxindex[2] || vertices[i].color == maxindex[3]) {
				hash6.put(vertices[i], new ArrayList<Vertex>());
			}
		}
		Vertex[] vertices1 = getVertices(hash1);
		Vertex[] vertices2 = getVertices(hash2);
		Vertex[] vertices3 = getVertices(hash3);
		Vertex[] vertices4 = getVertices(hash4);
		Vertex[] vertices5 = getVertices(hash5);
		Vertex[] vertices6 = getVertices(hash6);

		for (int i = 0; i <= vertices1.length - 1; i++) {
			Vertex v = vertices1[i];
			for (int j = i + 1; j <= vertices1.length - 1; j++) {
				Vertex u = vertices1[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					hash1.get(v).add(u);
					hash1.get(u).add(v);
				}
			}
		}

		for (int i = 0; i <= vertices2.length - 1; i++) {
			Vertex v = vertices2[i];
			for (int j = i + 1; j <= vertices2.length - 1; j++) {
				Vertex u = vertices2[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					hash2.get(v).add(u);
					hash2.get(u).add(v);
				}
			}
		}

		for (int i = 0; i <= vertices3.length - 1; i++) {
			Vertex v = vertices3[i];
			for (int j = i + 1; j <= vertices3.length - 1; j++) {
				Vertex u = vertices3[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					hash3.get(v).add(u);
					hash3.get(u).add(v);
				}
			}
		}
		for (int i = 0; i <= vertices4.length - 1; i++) {
			Vertex v = vertices4[i];
			for (int j = i + 1; j <= vertices4.length - 1; j++) {
				Vertex u = vertices4[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					hash4.get(v).add(u);
					hash4.get(u).add(v);
				}
			}
		}
		for (int i = 0; i <= vertices5.length - 1; i++) {
			Vertex v = vertices5[i];
			for (int j = i + 1; j <= vertices5.length - 1; j++) {
				Vertex u = vertices5[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					hash5.get(v).add(u);
					hash5.get(u).add(v);
				}
			}
		}
		for (int i = 0; i <= vertices6.length - 1; i++) {
			Vertex v = vertices6[i];
			for (int j = i + 1; j <= vertices6.length - 1; j++) {
				Vertex u = vertices6[j];
				if (!v.equals(u) && v.distanceFrom(u) <= r) {
					hash6.get(v).add(u);
					hash6.get(u).add(v);
				}
			}
		}

		List<Integer> edges = new ArrayList<>();
		RandomGeometricGraph[] graph = new RandomGeometricGraph[7];

		graph[1] = new RandomGeometricGraph(hash1, r);
		List<ArrayList<Vertex>> v01 = graph[1].displaygiantcomp(graph[1], vertices1);
		graph[1].v00 = v01;
		int edge1 = qqq(v01, graph[1], hash1);
		edges.add(edge1);

		graph[2] = new RandomGeometricGraph(hash2, r);
		List<ArrayList<Vertex>> v02 = graph[2].displaygiantcomp(graph[2], vertices2);
		graph[2].v00 = v02;
		edge1 = qqq(v02, graph[2], hash2);
		edges.add(edge1);

		graph[3] = new RandomGeometricGraph(hash3, r);
		List<ArrayList<Vertex>> v03 = graph[3].displaygiantcomp(graph[3], vertices3);
		graph[3].v00 = v03;
		edge1 = qqq(v03, graph[3], hash3);
		edges.add(edge1);

		graph[4] = new RandomGeometricGraph(hash4, r);
		List<ArrayList<Vertex>> v04 = graph[4].displaygiantcomp(graph[4], vertices4);
		graph[4].v00 = v04;
		edge1 = qqq(v04, graph[4], hash4);
		edges.add(edge1);

		graph[5] = new RandomGeometricGraph(hash5, r);
		List<ArrayList<Vertex>> v05 = graph[5].displaygiantcomp(graph[5], vertices5);
		graph[5].v00 = v05;
		edge1 = qqq(v05, graph[5], hash5);
		edges.add(edge1);

		graph[6] = new RandomGeometricGraph(hash6, r);
		List<ArrayList<Vertex>> v06 = graph[6].displaygiantcomp(graph[6], vertices6);
		graph[6].v00 = v06;
		edge1 = qqq(v06, graph[6], hash6);
		edges.add(edge1);

		// first backbone
		int max2 = edges.get(0);
		int maxindex2 = 0;
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i) > max2) {
				max2 = edges.get(i);
				maxindex2 = i;
			}
		}
		edges.set(maxindex2, -1);
		int color1 = 0;
		int color2 = 0;
		if (maxindex2 == 0) {
			color1 = maxindex[0];
			color2 = maxindex[1];
		} else if (maxindex2 == 1) {
			color1 = maxindex[0];
			color2 = maxindex[2];
		} else if (maxindex2 == 2) {
			color1 = maxindex[0];
			color2 = maxindex[3];
		} else if (maxindex2 == 3) {
			color1 = maxindex[1];
			color2 = maxindex[2];
		} else if (maxindex2 == 4) {
			color1 = maxindex[1];
			color2 = maxindex[3];
		} else if (maxindex2 == 5) {
			color1 = maxindex[2];
			color2 = maxindex[3];
		}

		adjacencyList = graph[maxindex2 + 1].adjacencyList;
		displayGraph(graph[maxindex2 + 1], color1, color2);
		reduce(graph[maxindex2 + 1]);

		// domination
		int count_nei = 0;
		int count_v = 0;
		double domination;
		Vertex[] finalvertices = getVertices(graph[maxindex2 + 1].adjacencyList);
		List<Vertex> finalvertex = new ArrayList<Vertex>();
		for (Vertex i : finalvertices) {
			if(i.gothrough==0){
				i.gothrough = 1;
				finalvertex.add(i);
			}		
			for (Vertex j : adja.get(i)) {
				if (j.gothrough == 0) {
					finalvertex.add(j);
					j.gothrough = 1;
				}
			}
		}	
		count_nei = finalvertex.size();
		domination = (double) (count_nei) / N;
		System.out.println("domination is :" + domination);

		System.out.println("backbone1 vertices: "+graph[maxindex2 + 1].adjacencyList.size());
		displayGraph(graph[maxindex2 + 1], color1, color2);
		int mostcolor;
		mostcolor = maxindex[0];
		System.out.println("Most color1: " + mostcolor);
		System.out.println("backbone1's edges: "+graph[maxindex2 + 1].getedges(graph[maxindex2 + 1]));;

		// second backbone
		max2 = edges.get(0);
		maxindex2 = 0;
		for (int i = 0; i < edges.size(); i++) {
			if (edges.get(i) > max2) {
				max2 = edges.get(i);
				maxindex2 = i;
			}
		}
		edges.set(maxindex2, -1);
		color1 = 0;
		color2 = 0;
		if (maxindex2 == 0) {
			color1 = maxindex[0];
			color2 = maxindex[1];
		} else if (maxindex2 == 1) {
			color1 = maxindex[0];
			color2 = maxindex[2];
		} else if (maxindex2 == 2) {
			color1 = maxindex[0];
			color2 = maxindex[3];
		} else if (maxindex2 == 3) {
			color1 = maxindex[1];
			color2 = maxindex[2];
		} else if (maxindex2 == 4) {
			color1 = maxindex[1];
			color2 = maxindex[3];
		} else if (maxindex2 == 5) {
			color1 = maxindex[2];
			color2 = maxindex[3];
		}

		adjacencyList = graph[maxindex2 + 1].adjacencyList;
		displayGraph(graph[maxindex2 + 1], color1, color2);
		reduce(graph[maxindex2 + 1]);

		// domination
		count_nei = 0;
		count_v = 0;
		domination = 0;
		
		finalvertices = getVertices(graph[maxindex2 + 1].adjacencyList);
		for (Vertex i : finalvertices) {
			if(i.gothrough==0){
				i.gothrough = 1;
				finalvertex.add(i);
			}		
			for (Vertex j : adja.get(i)) {
				if (j.gothrough == 0) {
					finalvertex.add(j);
					j.gothrough = 1;
				}
			}
		}
		count_nei = finalvertex.size();
		domination = (double) (count_nei) / N;
		System.out.println("domination is :" + domination);

		System.out.println("backbone2 vertices: "+graph[maxindex2 + 1].adjacencyList.size());
		displayGraph(graph[maxindex2 + 1], color1, color2);
		mostcolor = maxindex[1];
		System.out.println("Most color2: " + mostcolor);
		int edgess=0;
		System.out.println("backbone2's edges: "+graph[maxindex2 + 1].getedges(graph[maxindex2 + 1]));

	}

	public static void writetxt(String str, int a) {
		try {
			String filename = new String();
			filename = ".\\output" + String.valueOf(a) + ".txt";
			File writename = new File(filename); //
			writename.createNewFile(); // 创建新文件
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			out.write(str); // \r\n即为换行
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reduce(RandomGeometricGraph graph) {
		Hashtable<Vertex, List<Vertex>> hash1 = graph.adjacencyList;
		List<ArrayList<Vertex>> v01 = graph.v00;
		for (int i = 0; i < v01.size(); i++) {
			if (i != graph.maxindex) {
				for (Vertex v : v01.get(i)) {
					hash1.remove(v);
				}
			}
		}
	}

	public int qqq(List<ArrayList<Vertex>> v01, RandomGeometricGraph graph1, Hashtable<Vertex, List<Vertex>> hash1) {
		int max1 = v01.get(0).size();
		int maxindex1 = 0;
		for (int i = 0; i < v01.size(); i++) {
			if (v01.get(i).size() > max1) {
				max1 = v01.get(i).size();
				maxindex1 = i;
			}
		}
		graph1.maxindex = maxindex1;
		int edge1 = 0;
		for (Vertex v : v01.get(maxindex1)) {
			edge1 = edge1 + graph1.getAllNeighbors(hash1, v).size();
		}
		return edge1;
	}

	public List<ArrayList<Vertex>> displaygiantcomp(RandomGeometricGraph graph, Vertex[] vertices1) {
		List<ArrayList<Vertex>> v00 = new ArrayList<ArrayList<Vertex>>();
		List<Integer> e1 = new ArrayList<Integer>();
		graph.giantcomp(graph, vertices1[0], v00);
		for (int i = 0; i < vertices1.length; i++) {
			int z = 0;
			for (List list00 : v00) {
				if (list00.contains(vertices1[i])) {
					z = 1;
					break;
				}
			}
			if (z == 1)
				continue;
			else
				graph.giantcomp(graph, vertices1[i], v00);

		}
		return v00;

	}

	public List<ArrayList<Vertex>> giantcomp(RandomGeometricGraph graph, Vertex v, List<ArrayList<Vertex>> v00) {
		ArrayList<Vertex> v000 = new ArrayList<Vertex>();
		v000.add(v);
		int mm = v000.size();
		for (int i = 0; i < mm; i++) {
			List<Vertex> l1 = graph.getAllNeighbors(graph.adjacencyList, v000.get(i));
			for (Vertex o : l1) {
				if (!v000.contains(o))
					v000.add(o);
			}
			mm = v000.size();
		}
		v00.add(v000);
		return v00;
	}

	// Return the number of vertices of this graph
	public int getNumOfVertices(RandomGeometricGraph graph) {
		return graph.adjacencyList.size();
	}

	// Display this graph
	public void display(RandomGeometricGraph graph) {
		Vertex[] vertices = getVertices(graph.adjacencyList);
		// display all vertices
		System.out.println("[Vertices (x,y)]:");
		for (Vertex v : vertices) {
			System.out.printf("%s (%.3f,%.3f)\n", v.getLabel(), v.getX(), v.getY());
		}

		System.out.println("\n[Graph Edges as adjacency list]:");
		Iterator<Vertex> it = graph.adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex v = it.next();
			List<Vertex> neighbors = graph.adjacencyList.get(v);
			System.out.print(v.toString() + " -> ");
			for (Vertex u : neighbors) {
				System.out.print(u.getLabel() + " ");
			}
			System.out.println();
		}
	}

	// Compute and return the vertex with max degree
	public Vertex getVertexWithMaxDegree(RandomGeometricGraph graph) {
		int maxDegree = 0;
		Vertex v = null;

		Iterator<Vertex> it = graph.adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex u = it.next();
			if (maxDegree < graph.adjacencyList.get(u).size()) {
				maxDegree = graph.adjacencyList.get(u).size();
				v = u;
			}
		}
		return v;
	}

	public Vertex getVertexWithMaxDegree2(RandomGeometricGraph graph) {
		int maxDegree = 0;
		Vertex v = null;

		Iterator<Vertex> it = graph.adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex u = it.next();
			if (maxDegree < u.changedegree) {
				maxDegree = u.changedegree;
				v = u;
			}
		}
		return v;
	}

	// Compute and return the vertex with min degree
	public Vertex getVertexWithMinDegree(RandomGeometricGraph graph) {
		int minDegree = 99999;
		Vertex v = null;

		Iterator<Vertex> it = graph.adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex u = it.next();
			if (graph.adjacencyList.get(u).size() > 0 && (minDegree > graph.adjacencyList.get(u).size())) {
				minDegree = graph.adjacencyList.get(u).size();
				v = u;
			}
		}
		return v;
	}

	// Compute and return the average degree
	public double getAverageDegree(RandomGeometricGraph graph) {
		Iterator<Vertex> it = graph.adjacencyList.keySet().iterator();
		int s = 0;
		while (it.hasNext()) {
			Vertex u = it.next();
			s += graph.adjacencyList.get(u).size();

		}
		return s / graph.adjacencyList.size();
	}

	public void setVetxDeg() {
		Iterator<Vertex> it = adjacencyList.keySet().iterator();
		while (it.hasNext()) {
			Vertex u = it.next();
			u.degree = adjacencyList.get(u).size();
			u.changedegree = adjacencyList.get(u).size();
		}
	}

	// Return all vertices incident to a given vertex
	public List<Vertex> getAllNeighbors(Hashtable<Vertex, List<Vertex>> hash, Vertex v) {
		return hash.get(v);
	}

	// Create a new vertex with random location in the space (0,0,0) - (1,1,1)
	private Vertex createRandomVertex(String graphtype) {
		double x;
		double y;
		if (graphtype == "inOneHemisphere") {
			double z;
			z = Math.random();
			double theta = Math.random() * 2 * Math.PI;
			x = Math.sqrt(1 - Math.pow(z, 2)) * Math.cos(theta);
			y = Math.sqrt(1 - Math.pow(z, 2)) * Math.sin(theta);

			return new Vertex("v" + adjacencyList.size(), x, y, z);
		} else if (graphtype == "square") {
			x = Math.random();
			y = Math.random();
			return new Vertex("v" + adjacencyList.size(), x, y);
		} else if (graphtype == "disk") {
			double radios = Math.sqrt(Math.random());
			double theta = Math.random() * 2 * Math.PI;
			x = radios * Math.cos(theta);
			y = radios * Math.sin(theta);
			return new Vertex("v" + adjacencyList.size(), x, y);
		}
		return null;

	}

	public static void displayGraph(RandomGeometricGraph graph, int color1, int color2) {
		String title = String.format("Random Geometric Graph (%d vertices, r = %.3f)", graph.getNumOfVertices(graph),
				graph.getThresholdDistance());
		JFrame frame = new JFrame(title);
		DisplayGraph display = new DisplayGraph(graph, true, color1, color2);
		display.setPreferredSize(new Dimension(1500, 1500));
		frame.add(display, BorderLayout.CENTER);
		// frame.setPreferredSize(new Dimension(400, 400));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void displayGraphWithMinDegree(RandomGeometricGraph graph) {
		String title = String.format("Random Geometric Graph (%d vertices, r = %.3f)", graph.getNumOfVertices(graph),
				graph.getThresholdDistance());
		JFrame frame = new JFrame(title);
		DisplayGraph display = new DisplayGraph(graph, true);
		display.setPreferredSize(new Dimension(1500, 1500));
		frame.add(display, BorderLayout.CENTER);
		// frame.setPreferredSize(new Dimension(400, 400));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public int getedges(RandomGeometricGraph graph) {
		int edges = 0;
		for (Vertex i : graph.adjacencyList.keySet()) {
			edges += graph.adjacencyList.get(i).size();
		}
		return edges / 2;
	}

	public static void main(String args[]) {

		RandomGeometricGraph graph = new RandomGeometricGraph(4000, 128, "disk");
		graph.setdegreeList(graph);
		displayGraphWithMinDegree(graph);
		graph.display(graph);
		System.out.println("number of efges: " + graph.getedges(graph));
		System.out.println("min degree: " + graph.getVertexWithMinDegree(graph).degree);
		System.out.println("Average degree: " + graph.getAverageDegree(graph));
		System.out.println("max degree: " + graph.getVertexWithMaxDegree(graph).degree);

		graph.smalllastordering(graph);
		System.out.println("max degree when deteled: " + graph.getVertexWithMaxDegree2(graph).changedegree);

	}
}
