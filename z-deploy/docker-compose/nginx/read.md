模式,优先级从高到底	
location =/url	    =  开头表示精确匹配,只有匹配上才生效
location ^~/url	    ^~ 开头表示前缀匹配
location ~pattern	~ 开头表示区分大小写的正则匹配
location ~*pattern	~*开头表示不区分大小写的正则匹配
location /url	    不带任何开头的也表示前缀匹配，但是优先级在正则匹配之后
location /	        通配匹配，任何未匹配到其他location的请求都会匹配到， 相当于switch的default