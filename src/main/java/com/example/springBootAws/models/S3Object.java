package com.example.springBootAws.models;

import java.util.Objects;

public class S3Object {

 private String arn;
 private String key;
 private String bucket;
 private String name;
 private long size;
 private byte[] content; 

 public S3Object(String arn, String key, String bucket, String name, long size, byte[] content) {
  this.arn = arn;
  this.key = key;
  this.bucket = bucket;
  this.name = name;
  this.size = size;
  this.content = content;
 }

 public S3Object() {
 }

 public S3Object(String key, String name, byte[] content) {
	  this.key = key;
	  this.bucket = bucket;
	  this.name = name;
	  this.content = content;
}

 @Override
 public boolean equals(Object o) {
  if (this == o)
   return true;
  if (!(o instanceof S3Object))
   return false;
  S3Object s3Object = (S3Object) o;
  return Objects.equals(arn, s3Object.arn)
   && Objects.equals(key, s3Object.key)
   && Objects.equals(bucket, s3Object.bucket)
   && Objects.equals(name, s3Object.name)
   && Objects.equals(size, s3Object.size)
   && Objects.equals(content, s3Object.content);
 }

 @Override
 public int hashCode() {
  return Objects.hash(arn, key, bucket, name, size, content);
 }

 @Override
 public String toString() {
  return "S3 Object{arn=" + arn + ", key=" + key + ", bucket=" + bucket + ", name=" + name + ", size=" + size + "}";
 }

	public String getArn() {
		return arn;
	}
	
	public void setArn(String arn) {
		this.arn = arn;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getSize() {
		return size;
	}
	
	public void setSize(long size) {
		this.size = size;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	} 
}
