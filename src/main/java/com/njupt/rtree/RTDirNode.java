package com.njupt.rtree;  
  
import java.util.ArrayList;  
import java.util.List;  
  
import com.njupt.constants.Constants;  
  
/** 
 * @ClassName RTDirNode  
 * @Description ��Ҷ�ڵ� 
 */  
public class RTDirNode extends RTNode  
{  
    /** 
     * ���ӽ�� 
     */  
    protected List<RTNode> children;  
      
    public RTDirNode(RTree rtree, RTNode parent, int level)   
    {  
        super(rtree, parent, level);  
        children = new ArrayList<RTNode>();  
    }  
      
    /** 
     * @param index 
     * @return ��Ӧ�����µĺ��ӽ�� 
     */  
    public RTNode getChild(int index)  
    {  
        return children.get(index);  
    }  
  
    @Override  
    public RTDataNode chooseLeaf(Rectangle rectangle)   
    {  
        int index;  
        index = findLeastEnlargement(rectangle);    
          
        insertIndex = index;//��¼����·��������  
        return getChild(index).chooseLeaf(rectangle);  
    }  
  
    /** 
     * @param rectangle 
     * @return ���������С�Ľ������������������������ѡ�����������С�� 
     */  
    private int findLeastEnlargement(Rectangle rectangle)   
    {  
        double area = Double.POSITIVE_INFINITY;  
        int sel = -1;  
          
        for(int i = 0; i < usedSpace; i ++)  
        {  
            double enlargement = getDatas()[i].getUnionRectangle(rectangle).getArea() - getDatas()[i].getArea();  
            if(enlargement < area)  
            {  
                area = enlargement;  
                sel = i;  
            }else if(enlargement == area)  
            {  
                sel = (getDatas()[sel].getArea() < getDatas()[i].getArea()) ? sel : i;  
            }  
        }  
          
        return sel;  
    }  
  
    /** 
     * �����µ�Rectangle��Ӳ����Ҷ�ڵ㿪ʼ���ϵ���RTree��ֱ�����ڵ� 
     * @param node1 ������Ҫ�����ĺ��ӽ�� 
     * @param node2  ���ѵĽ�㣬��δ������Ϊnull 
     */  
    public void adjustTree(RTNode node1, RTNode node2)   
    {  
        //��Ҫ�ҵ�ָ��ԭ���ɵĽ�㣨��δ���Rectangle֮ǰ������Ŀ������  
        getDatas()[insertIndex] = node1.getNodeRectangle();//����node1����ԭ���Ľ��  
        children.set(insertIndex, node1);//�滻�ɵĽ��  
          
        if(node2 != null)  
        {  
            insert(node2);//�����µĽ��  
              
        }else if(! isRoot())//��û������ڵ�  
        {  
            RTDirNode parent = (RTDirNode) getParent();  
            parent.adjustTree(this, null);//���ϵ���ֱ�����ڵ�  
        }  
    }  
  
    /** 
     * @param node 
     * @return ��������Ҫ�����򷵻�true 77
     */  
    protected boolean insert(RTNode node)   
    {  
        if(usedSpace < rtree.getNodeCapacity())  
        {  
            getDatas()[usedSpace ++] = node.getNodeRectangle();  
            children.add(node);//�¼ӵ�  
            node.parent = this;//�¼ӵ�  
            RTDirNode parent = (RTDirNode) getParent();  
            if(parent != null)  
            {  
                parent.adjustTree(this, null);  
            }  
            return false;  
        }else{//��Ҷ�ӽ����Ҫ����  
            RTDirNode[] a = splitIndex(node);  
            RTDirNode n = a[0];  
            RTDirNode nn = a[1];  
              
            if(isRoot())  
            {  
                //�½����ڵ㣬������1  
                RTDirNode newRoot = new RTDirNode(rtree, Constants.RTNode_NULL, getLevel() + 1);  
                  
                //����Ҫ��ԭ�����ĺ�����ӵ��������ѵĽ��n��nn�У���ʱn��nn�ĺ��ӽ�㻹Ϊ��  
//              for(int i = 0; i < n.usedSpace; i ++)  
//              {  
//                  n.children.add(this.children.get(index));  
//              }  
//                
//              for(int i = 0; i < nn.usedSpace; i ++)  
//              {  
//                  nn.children.add(this.children.get(index));  
//              }  
                  
                //���������ѵĽ��n��nn��ӵ����ڵ�  
                newRoot.addData(n.getNodeRectangle());  
                newRoot.addData(nn.getNodeRectangle());  
                  
                newRoot.children.add(n);  
                newRoot.children.add(nn);  
                  
                //�����������ѵĽ��n��nn�ĸ��ڵ�  
                n.parent = newRoot;  
                nn.parent = newRoot;  
                  
                //�������rtree�ĸ��ڵ�  
                rtree.setRoot(newRoot);//�¼ӵ�  
            }else {  
                RTDirNode p = (RTDirNode) getParent();  
                p.adjustTree(n, nn);  
            }  
        }  
        return true;  
    }  
  
    /** 
     * ��Ҷ�ӽ��ķ��� 
     *  
     * @param node 
     * @return 
     */  
    private RTDirNode[] splitIndex(RTNode node)   
    {  
        int[][] group = null;  
        group = quadraticSplit(node.getNodeRectangle());  
        children.add(node);//�¼ӵ�  
        node.parent = this;//�¼ӵ�   

          
        RTDirNode index1 = new RTDirNode(rtree, parent, getLevel());  
        RTDirNode index2 = new RTDirNode(rtree, parent, getLevel());  
          
        int[] group1 = group[0];  
        int[] group2 = group[1];  
          
        for(int i = 0; i < group1.length; i ++)  
        {  
            //Ϊindex1������ݺͺ���  
            index1.addData(getDatas()[group1[i]]);  
            index1.children.add(this.children.get(group1[i]));//�¼ӵ�  
            //��index1��Ϊ�丸�ڵ�  
            this.children.get(group1[i]).parent = index1;//�¼ӵ�  
        }  
        for(int i = 0; i < group2.length; i ++)  
        {  
            index2.addData(getDatas()[group2[i]]);  
            index2.children.add(this.children.get(group2[i]));//�¼ӵ�  
            this.children.get(group2[i]).parent = index2;//�¼ӵ�  
        }  
          
        return new RTDirNode[]{index1,index2};  
    }  
    
    @Override
    protected List<Rectangle> search(Rectangle rectangle) {
    	// TODO Auto-generated method stub
    	List<Rectangle> list=new ArrayList<Rectangle>();
    	for(int i = 0; i < usedSpace; i ++)  
        {  
            if(rectangle.isIntersection(getDatas()[i]))  
            {  
               // deleteIndex = i;//��¼����·��  
            	RTNode node=children.get(i);
            	List<Rectangle> a=node.search(rectangle);
            	for(int j = 0; j < a.size(); j ++)  
                {  
                     list.add(a.get(j));  
                }  
            }  
        }  
    	return list;
    }
  
}