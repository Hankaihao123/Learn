package com.hkh.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TreeNode {
	private Integer id; // 当前id
	@JsonProperty(value = "parentId")
	@JSONField(name = "parentId")
	private Integer pid;// 父id
	private String title;// 标题
	private String icon;// 图标
	private String href;// url地址
	private Boolean spread;// 是否展开
	private List<TreeNode> children = new ArrayList<TreeNode>();
	
	//分配权限时使用
	private String checkArr="0";//0代表无没有 1代表有
	
	// 初始化dtree时使用
	public TreeNode(Integer id, Integer pid, String title, Boolean spread) {
		super();
		this.id = id;
		this.pid = pid;
		this.title = title;
		this.spread = spread;
	}

	public TreeNode(Integer id, Integer pid, String title, String icon, String href, Boolean spread) {
		super();
		this.id = id;
		this.pid = pid;
		this.title = title;
		this.icon = icon;
		this.href = href;
		this.spread = spread;
	}

	// 将一个 List<TreeNode>转换成树状,首页菜单使用
	public static List<TreeNode> convertTreeNodeList(List<TreeNode> treeNodes, Integer pid) {
		ArrayList<TreeNode> resultNodes = new ArrayList<TreeNode>();
		for (TreeNode node1 : treeNodes) {
			Integer pid2 = node1.getPid();
			if (pid2 == pid) {
				resultNodes.add(node1);
			}
			for (TreeNode node2 : resultNodes) {
				if (node1.getPid() == node2.getId()) {
					node2.children.add(node1);
				}
			}
		}
		return resultNodes;
	}
	
	

}
