package com.zkl.memruss.control.settings

import android.content.Context

object AppVersion {
	
	fun getVersion(context: Context) {
		val manager = context.packageManager
		val info = manager.getPackageInfo(context.packageName, 0)
		return info.run { versionCode to versionName }
	}
	
	val updateLogs = arrayOf(
//			"- 优化了笔记本夹的跳转机制？\n", //todo
//			"- 加了设置界面？\n", //todo
		
		"version alpha-27:\n2018-3-21\n" +
			"小更新，修复了搜索界面快速返回会崩溃的bug\n"
		,
		"version alpha-26:\n2018-2-11\n" +
			"- 优化了一些UI\n" +
			"- 修复了一些bug\n" +
			"- 优化了词条复习顺序\n"
		,
		"version alpha-25:\n2018-1-4\n" +
			"跨年撒花 %°*:.☆(￣▽￣)/$:*.°★\n" +
			"- 加回了通过文本的方式添加词条的功能（注意格式有变）\n" +
			"- 修复了一些之前导入单词本的bug\n" +
			"- 优化了一些UI\n"
		,
		"version alpha-24:\n2017-12-30\n" +
			"新版的一些维护性更新\n" +
			"- 恢复了优先新的词条加入计划的机制（之前忘记了）\n" +
			"- 在复习计划界面显示了一些统计信息\n" +
			"- 改了一些图标\n"
		,
		"version alpha-23:\n2017-12-29\n" +
			"哦时隔一年，终于更新了！\n" +
			"这次几乎重写了整个程序，改动太大，连包名都改了。\n" +
			"具体有什么不同自己去体验吧！\n"
		,
		"version alpha-22:\n2016-10-10\n" +
			"小更新，改了新词添加的机制，晚加入的新词会被优先加入计划\n" +
			"大改还有好久啊，没时间写这个啊……\n"
		,
		"version alpha-21:\n2016-8-15\n" +
			"又修复bug。\nbug有点多哈，抓半天抓不完……\n" +
			"修复了某些时候workLoad计算错误的问题\n" +
			"最近都没怎么写这个程序，除了修复bug以外就被干嘛了……又一次大改酝酿中，但有没有时间就不知道了。\n"
		,
		"version alpha-20:\n2016-8-11\n" +
			"这次是加入新功能啦哈，\n不是bug拉哈！\n" +
			"- 现在可以调节工作量啦，此功能可以智能防生词过量积累\n" +
			"然后就没啥了，这可能是有一次大改之前的最后一次常规功能更新\n"
		,
		"version alpha-19:\n2016-8-10\n" +
			"又是修复bug……\n" +
			"（这个bug还是上次改程序导致的……）\n" +
			"唉，整天修复bug……\n"
		,
		"version alpha-18:\n2016-8-2\n" +
			"虽然时隔一个月，但这不是什么大更新，只是修复bug\n" +
			"（比如点“没记住”可能导致学习进度变成负数）\n" +
			"暑假没啥时间搞这个项目，所以呵呵大家就继续找bug吧……\n"
		,
		"version alpha-17:\n2016-6-30\n" +
			"紧急更新\n" +
			"修复一些复习进度计算的巨大bug\n（比如按“忘记了”进度会增加）\n" +
			"在此深表歉意……\n"
		,
		"version alpha-16!!:\n2016-6-27\n" +
			"重要的大更新！！\n" +
			"在之前的一个月那么长的时间里重写了软件的核心组件！\n" +
			"（核心重写就意味着整个软件差不多都要重写啊啊啊）\n" +
			"累死宝宝了……\n" +
			"虽然UI和功能还没有什么更新，但整个软件的功能和性能都将有重大提升，期待吧宝贝！\n"
		,
		"version alpha-15:\n2016-5-25\n" +
			"一次解决bug的紧急更新：\n" +
			"解决了之前存在的“修改”或“删除”在某些条件下出错的大bug\n"
		,
		"version alpha-14:\n2016-5-24\n" +
			"- 修复了搜索时闪退的一个bug\n" +
			"- 调整了计划设置，终于可以实现“暂停”啦\n" +
			"- 发现了“修改”时可能出现的bug\n（还不知道有没有修好……）\n" +
			"好啦就一次小更新，别指望太多！\n"
		,
		"version alpha-13:\n2016-5-21\n" +
			"- 不再支持.zrsd 格式文件的读取\n" +
			"- 增加了合并单词本的功能哦！\n" +
			"- 改进了重复查询功能和修改功能\n" +
			"（为了处理这几种冲突，写了一堆堆东西，累s了！）\n"
		,
		"version alpha-12:\n2016-5-17\n" +
			"- 改进了动画效果哦！\n" +
			"- 修复了若干个小bug（其实是很多个）\n" +
			"没了……\n"
		,
		"version alpha-11:\n2016-5-11\n" +
			"- 改进了修改note界面对格式错误的处理方法\n" +
			"- 重写了UI架构，从而解决了一个小bug\n尽管你还是不会看到什么变化……\n"
		,
		"version alpha-10:\n2016-5-11\n" +
			"- 重写了note的内核，读取导出的效率更高了！\n" +
			"然后就没有了，你看不到任何变化……\n —_—|| \n"
		,
		"version alpha-9:\n2016-5-5\n" +
			"这次的更新内容比较少\n" +
			"- 导入文件时可以直接导入到笔记本中（不用再新建笔记本）\n" +
			"- 放弃了.zrst扩展名，今后全用.txt\n" +
			"- 优化了文件读取，不需要标头也可以读取\n" +
			"- 调整了双击退出的间隔时间\n" +
			"- 调整了部分机型上的UI布局问题\n"
		,
		"version alpha-8:\n2016-4-29\n" +
			"经过郑工程师的奋战，时隔将近一个月，终于再次更新啦！\n（都怪太极拳！）\n\n" +
			"- 可以导出和导入数据库文件了！\n （这样就可以备份学习计划了）\n" +
			"- 改了输入词条的格式，更简洁方便\n （在输入框中将不支持原格式，原导出成文件的文本格式仍支持导入，但后续版本可能取消支持）\n" +
			"- 本宝宝终于有图标了！好不好看！\n" +
			"- 显然，更新日志的界面换了\n" +
			"- 修复了屏幕旋转时的崩溃的bug\n"
		,
		"version alpha-7:\n2016-4-4\n" +
			"- 重磅！经过多天的努力终于完成根据记忆曲线制定学习计划的功能！！\n" +
			"- 优化了添加词条时输入法自动弹出的问题\n" +
			"- 修复了切换动画时的某些bug\n"
		,
		"version alpha-6:\n2016-3-26\n" +
			"- Note支持查重啦！在输入重复单词的时候会提示并解决重复！\n" +
			"- 修复了快速按返回键时在部分机型上会出的bug\n" +
			"- 另外增加了双击退出，修改了部分UI界面\n"
		,
		"version alpha-5:\n" +
			"- Note支持搜索啦！" +
			"（这可是我跟多线程奋力搏斗的结果……）\n" +
			"- 另外改善了代码，动画变得更流畅了。\n" +
			"- 另外还改了Note的显示顺序，原来乱乱的……\n"
		,
		"version alpha-4:\n" +
			"对UI框架大改了一番（虽然你并不会看到什么不同\uD83D\uDE02……）\n" +
			"哈哈哈是不是失望了呢？嘿嘿……\n"
		,
		"version alpha-3:\n" +
			"- 补上了文件导入功能\uD83D\uDE02\n" +
			"（功能略微粗糙，谅解了哈\uD83D\uDE02）\n" +
			"- 另外修复了“修改note”功能的bug\n"
		,
		"version alpha-2:\n" +
			"- 修复了右上角版本信息按钮bug的问题。\n" +
			"- 增加了导出到文件的功能（注意！现在还没有导入文件的功能！\uD83D\uDE02）\n"
		,
		"version alpha-1:\n" +
			"第一个alpha测试版本！\n" +
			"只做了note部分的简略功能，只能顺序浏览词条，其他功能后续开发！\n"
	
	)
}
