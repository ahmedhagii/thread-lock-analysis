- take couple lines before the lock to show which code blocks lead to certain locks
- line after -> code block where lock is used -> will be the reason for hogging locked resource
- lines after just from 


clustering:
- for each line of stack trace, split by '.', first 4 form a unique cluster
- rescan the trace replacing each line by its cluster number
- will end up with a seq like 1, 1, 1, 2, 2, 3, 3, 2, 2, 2, 5
- merge the seq to get 1,2,3,2,5
- now form a graph whose nodes are the clusters and edges are the seq above
- locks lines will be nodes too and will fit in the sequence. color them differently if it's locked ot waiting to lock 

- if we wanna use clustering algorithms, we can split by '.', and each token with its index will be a feature, then run the alg
- proceed with assigning lines to clusters from the method above



- waiting to lock
- parking to wait for
- locked
- waiting on (Object.wait() ?


meeting notes:
- thresohld controller on sankey diagram)
- stacked area chart instead of streamgraph (more readable)
- shared toolip
- click on resource in stacked area, directs to sankey for that resource
- rangeSelector (used as global timefarme selector)
- global legend, filter for packages
- package split depth (customizatble)
- number of code blocks before and after a locked



