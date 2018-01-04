M = csvread("output.txt");
x = ones(1,19020);
y = ones(1,19020);
z = ones(1,19020);


for c = 1:19020
   x(c) = M(c,1); 
   y(c) = M(c,2);  
   z(c) = M(c,3); 
end

scatter3(x,y,z,'.')
