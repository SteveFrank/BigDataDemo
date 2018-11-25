package com.demo.scala

import com.sun.tools.javac.code.Attribute.Array

object VariableDemo extends OutOfMemoryError {
  def main(args: Array[String]): Unit = {
    var i = 1
    var s = "Hello"
    var str : String = "demo"
    print(i , s, str)
  }

}