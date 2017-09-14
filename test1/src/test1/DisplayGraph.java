package test1;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import javax.swing.JPanel;

// This class display a random geometric graph
public class DisplayGraph extends JPanel {

	private RandomGeometricGraph graph;
	private boolean withMinDegree = true;
	private int color1 = 0;
	private int color2 = 0;

	public DisplayGraph(RandomGeometricGraph graph, boolean withMinDegreeEdges) {
		this.graph = graph;
		this.withMinDegree = withMinDegreeEdges;
		// this.setSize(400, 400);
	}

	public DisplayGraph(RandomGeometricGraph graph, boolean withMinDegreeEdges, int color1, int color2) {
		this.graph = graph;
		this.withMinDegree = withMinDegreeEdges;
		this.color1 = color1;
		this.color2 = color2;
		// this.setSize(400, 400);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		int WIDTH = this.getWidth();
		int HEIGHT = this.getHeight();
		if (graph.type == 0 && graph != null) {
			g2d.setStroke(new BasicStroke(0.1f));
			Vertex minDegVertex = graph.getVertexWithMinDegree(graph);
			Vertex maxDegVertex = graph.getVertexWithMaxDegree(graph);
			Vertex[] vertices = graph.getVertices(graph.adjacencyList);
			for (int i = 0; i < vertices.length; i++) {
				Vertex v = vertices[i];
				if (v.equals(minDegVertex)) {
					g.setColor(Color.yellow);
				} else if (v.equals(maxDegVertex)) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.white);
				}
				int vx = WIDTH / 2+ (int) (v.getX() * WIDTH / 3);
				int vy = HEIGHT / 2 + (int) (v.getY() * HEIGHT / 3);
				g.fillOval(vx, vy, 5, 5);
			}
			for (int i = 0; i < vertices.length; i++) {
				Vertex v = vertices[i];
				int vx = WIDTH / 2 + (int) (v.getX() * WIDTH / 3);
				int vy = HEIGHT / 2 + (int) (v.getY() * HEIGHT / 3);
				List<Vertex> neighbors = graph.getAllNeighbors(graph.adjacencyList, v);
				if (neighbors != null) {
					for (int j = 0; j < neighbors.size(); j++) {
						Vertex u = neighbors.get(j);
						int ux = WIDTH / 2 + (int) (u.getX() * WIDTH /3);
						int uy = HEIGHT / 2 + (int) (u.getY() * HEIGHT / 3);
						if(v.equals(maxDegVertex)||u.equals(maxDegVertex)){
							g.setColor(Color.RED);
							g2d.setStroke(new BasicStroke(1f));
							g.drawLine(vx, vy, ux, uy);
						}
						if (v.equals(minDegVertex)||u.equals(minDegVertex)) {
							g.setColor(Color.yellow);
							g2d.setStroke(new BasicStroke(1f));
							g.drawLine(vx, vy, ux, uy);
						}
						else{
							g.setColor(Color.white);
							g2d.setStroke(new BasicStroke(0.01f));
							 g.drawLine(vx, vy, ux, uy);
						}
						/*
						g.setColor(Color.YELLOW);
						g.fillOval(ux, uy, 10, 10);
						if (v.equals(minDegVertex)) {
							g.setColor(Color.BLUE);
							g.drawLine(vx, vy, ux, uy);
						}
						if (v.equals(maxDegVertex)) {
							g.setColor(Color.RED);
							g.drawLine(vx, vy, ux, uy);
						} else {
							g.setColor(Color.yellow);
							g.drawLine(vx, vy, ux, uy);
						}
						*/

						// g.drawLine(vx, vy, ux, uy);
					}
				}

			}

		
	}else if(graph.type==1&&graph!=null)

	{
		g2d.setStroke(new BasicStroke(0.5f));
		Vertex[] vertices = graph.getVertices(graph.adjacencyList);
		for (int i = 0; i < vertices.length; i++) {
			Vertex v = vertices[i];
			if (v.color == color1) {
				g2d.setColor(Color.pink);
			} else if (v.color == color2) {
				g2d.setColor(Color.yellow);
			}
			int vx = WIDTH / 2 + (int) (v.getX() * WIDTH / 3);
			int vy = HEIGHT / 2 + (int) (v.getY() * HEIGHT / 3);
			g2d.fillOval(vx, vy, 6,6);
			List<Vertex> neighbors = graph.getAllNeighbors(graph.adjacencyList, v);
			if (neighbors != null) {
				for (int j = 0; j < neighbors.size(); j++) {
					Vertex u = neighbors.get(j);
					int ux = WIDTH / 2 + (int) (u.getX() * WIDTH / 3);
					int uy = HEIGHT / 2 + (int) (u.getY() * HEIGHT / 3);
					if (u.color == color1) {
						g2d.setColor(Color.pink);
					} else if (u.color == color2) {
						g2d.setColor(Color.yellow);
					}
					g2d.fillOval(ux, uy, 4, 4);
					/*
					 * if(v.equals(minDegVertex) || u.equals(minDegVertex)) {
					 * g2d.setColor(Color.green); g2d.drawLine(vx, vy, ux, uy);
					 * } if(v.equals(maxDegVertex)||u.equals(maxDegVertex)){
					 * g2d.setColor(Color.red); g2d.drawLine(vx, vy, ux, uy); }
					 */

					// g.setColor(Color.black);
					g2d.drawLine(vx, vy, ux, uy);
				}
			}
		}
	}
	}
}