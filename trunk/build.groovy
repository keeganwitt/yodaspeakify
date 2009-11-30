def ant = new AntBuilder().sequential {
	taskdef name: "groovyc", classname: "org.codehaus.groovy.ant.Groovyc"
	groovyc srcdir: "src", destdir: "war/WEB-INF/classes/com/appspot/yodaspeakify", {
		classpath {
			fileset dir: "war/WEB-INF/lib", {
		    	include name: "*.jar"
			}
			pathelement path: "war/WEB-INF/classes/com/appspot/yodaspeakify"
		}
		javac source: "1.6", target: "1.6", debug: "on"
	}
}
