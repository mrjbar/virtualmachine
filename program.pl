#load y, x ~ 1 // y = max(x-1, 0)
#load x 2
#load w 0
#loop x
#  load v w
#  inc w
#end
#load y v

#load x, !y // x = (0 < x)?0:1
#load yes 1
#load x 1
#loop yes
#   load x 0
#end
#max x - 1
load y 2
load x 10
load v x
loop y
	load w 0
	loop x
	   load v w
	   inc w
	end
	load x v
end
load result v
