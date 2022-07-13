# Dependency-Graph-Generator
Work-in-progress

Pending rewriting into not Java and a better scalable project paradigm

Generate a graph of dependencies leading from program entry point to all included assets

At current can generate a fullscale graph of a project without text.

https://github.com/ABViney/Dependency-Graph-Generator/blob/main/DGG-Example.png  
Dgg-Example  
C++ -- A main function, two header/source files and iostream  
System libraries (anything in '<...>') are represented in a vertical stack on the left.  
Project files (anything in '"..."') are represented in the stacked structure on the right.  
The entrypoint of the application is at the bottom-right --  
  (I didn't realize that BMP is an outlier when it comes to rendering file data, so graphs are 'technically' inverted.)  
Edges between graphs retain the color of their parent.  

https://github.com/ABViney/Dependency-Graph-Generator/blob/main/AngryGLMap.png  
AngryGL  
C++ -- Built using clone from https://github.com/ntcaston/AngryGL  
Better shows off the beauty of my mapping algorithm. Pretty even without  

Limitations at this point are project size specific.  
Nigh-entire reason I begain building this was to break down and understand this repo : https://github.com/cortex-command-community/Cortex-Command-Community-Project-Source --  
but at current it is too large to render the entirety of, even for a PNG.

Use isn't exclusive to C/C++, but it is what I'm building it for.
