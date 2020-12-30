package com.njupt.rtree;

import java.util.ArrayList;
import java.util.List;

import com.njupt.constants.Constants;

/**
 * @ClassName RTDataNode
 * @Description
 */
public class RTDataNode extends RTNode {

	public RTDataNode(RTree rTree, RTNode parent) {
		super(rTree, parent, 0);
	}

	/**
	 * 在叶节点中插入Rectangle，插入后如果其父节点不为空则需要向上调整树直到根节点；<br>
	 * 若插入Rectangle之后超过结点容量则需要分裂结点
	 * 
	 * @param rectangle
	 * @return
	 */
	public boolean insert(Rectangle rectangle) {
		if (usedSpace < rtree.getNodeCapacity()) {
			getDatas()[usedSpace++] = rectangle;
			RTDirNode parent = (RTDirNode) getParent();

			if (parent != null)
				parent.adjustTree(this, null);
			return true;

		} else {// 超过结点容量
			RTDataNode[] splitNodes = splitLeaf(rectangle);
			RTDataNode l = splitNodes[0];
			RTDataNode ll = splitNodes[1];

			if (isRoot()) {
				// 根节点已满，需要分裂。创建新的根节点
				RTDirNode rDirNode = new RTDirNode(rtree, Constants.RTNode_NULL, getLevel() + 1);
				rtree.setRoot(rDirNode);
				rDirNode.addData(l.getNodeRectangle());
				rDirNode.addData(ll.getNodeRectangle());

				ll.parent = rDirNode;
				l.parent = rDirNode;

				rDirNode.children.add(l);
				rDirNode.children.add(ll);

			} else {// 不是根节点
				RTDirNode parentNode = (RTDirNode) getParent();
				parentNode.adjustTree(l, ll);
			}

		}
		return true;
	}

	/**
	 * 插入Rectangle之后超过容量需要分裂
	 * 
	 * @param rectangle
	 * @return
	 */
	public RTDataNode[] splitLeaf(Rectangle rectangle) {
		int[][] group = null;
		group = quadraticSplit(rectangle);
		

		RTDataNode l = new RTDataNode(rtree, parent);
		RTDataNode ll = new RTDataNode(rtree, parent);

		int[] group1 = group[0];
		int[] group2 = group[1];

		for (int i = 0; i < group1.length; i++) {
			l.addData(getDatas()[group1[i]]);
		}

		for (int i = 0; i < group2.length; i++) {
			ll.addData(getDatas()[group2[i]]);
		}
		return new RTDataNode[] { l, ll };
	}

	@Override
	public RTDataNode chooseLeaf(Rectangle rectangle) {
		insertIndex = usedSpace;// 记录插入路径的索引
		return this;
	}
	
	@Override
	protected List<Rectangle> search(Rectangle rectangle) {
		// TODO Auto-generated method stub
		List<Rectangle> list=new ArrayList<Rectangle>();
		for(int i=0;i<usedSpace;i++){
			if(rectangle.enclosure(getDatas()[i])){
				list.add(getDatas()[i]);
			}
		}
		return list;
	}

}