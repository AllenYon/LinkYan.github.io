---
layout: post
title:  Python中参数是传值还是传引用？
---

先看一个例子

```
>>> def try_to_change(n):
	n = 'Mr.Gumby'
	
>>> name = 'Mrs.Entity'
>>> try_to_change(name)
>>> name
'Mrs.Entity'
```

当我们的参数是不可变对象（如数字，字符或者元组）时，是传引用的。我们可以把过程详细成如下操作：

```
>>> name = 'Mrs.Entity'
>>> n = name
>>> n = 'Mr.Gumby'
>>> name
'Mrs.Entitiy'
```
那么当参数是可变对象（如列表，字典）时，看下面的例子貌似又是“传值”了？

```
>>> def change(n):
		n[0] = 'Mr.Gumby'
		
>>> names = ['Mrs.Entity', 'Mrs.Thing']
>>> change(names)
>>> names
['Mr.Gumby','Mrs.Thing']
```

我们再把参数传递过程写出来：

```
>>> names = ['Mrs.Entity', 'Mrs.Thing']
>>> n = names
>>> n[0] = 'Mr.Gumby'
>>> names
['Mr.Gumby', 'Mrs.Thing']
```
我们知道当两个变量同时引用一个列表时，就是指向了同一对象，所以才会发生传递的参数值改变了！为了避免这种情况，就需要复制一个列表的副本，我们可以使用到切片。

```
>>> names = ['Mrs.Entity', 'Mrs.Thing']
>>> n = names[:]
```

```
>>> n is names
False
>>> n == names
True
```

```
>>> n[0] = 'Mr.Gumby'
>>> n
['Mr.Gumby', 'Mrs.Thing']
>>> names
['Mrs.Entity', 'Mrs.Thing']
```
现在我们再调用change()函数试试看：

```
>>> change(names[:])
>>> names
['Mrs.Entity', 'Mrs.Thing']
```

###总结：Python是参数传递采用的传引用方式。























 
 
 

