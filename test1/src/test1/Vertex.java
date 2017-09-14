package test1;
import java.util.Objects;


public class Vertex {
    
    private double x,y,z;
    private String label;
    public int degree;
    public int changedegree;
    public Integer color;
    public int gothrough;
    
    public Vertex(String label,double x,double y){
    	this.x=x;
    	this.y=y;
    	this.label=label;
    	color=0;
    	gothrough=0;
    }
    
    public Vertex(String label, double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
        this.label = label;
        color=0;
        gothrough=0;
    }

    public String getLabel() {
        return label;
    }
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }        
    
    public double distanceFrom(Vertex v){
        return Math.sqrt( (x-v.x)*(x-v.x) + (y-v.y)*(y-v.y)+ (z-v.z)*(z-v.z));
        //return Math.sqrt( (x-v.x)*(x-v.x) + (y-v.y)*(y-v.y));
    }
       
    @Override
    public boolean equals(Object o){
        if(!(o instanceof Vertex))
            return false;
        
        Vertex v = (Vertex)o;
        return v.label.equals(this.label);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.label);
        return hash;
    }
    
    @Override
    public String toString(){
        return label + " ("+
                String.format("%.3f",x)+","+
                String.format("%.3f",y)+")";
    }
}