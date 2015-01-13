package com.ijob.spider.queue;

import java.util.Collection;
import java.util.LinkedList;

public class UrlQueue {
	// 超链接队列
	private static LinkedList<String> urlQueue = new LinkedList<String>();

	public synchronized static void addElement(String url){
		urlQueue.add(url);
	}
	
	public synchronized static void addFirstElement(String url){
		urlQueue.addFirst(url);
	}
	
	public synchronized static String outElement(){
		return urlQueue.removeFirst();
	}
	
	public synchronized static void addCollections(Collection<? extends String> collection){
		urlQueue.addAll(collection);
	}
	
	public synchronized static void addCollections(int index, Collection<? extends String> collection){
		urlQueue.addAll(index, collection);
	}
	
	public static boolean isEmpty(){
		return urlQueue.isEmpty();
	}
	
	public static int size(){
		return urlQueue.size();
	}
	
	public static boolean isContains(String url){
		return urlQueue.contains(url);
	}
}
