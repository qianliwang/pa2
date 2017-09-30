# COMS 535 Programming Assignment 2 MinHash
We implement the MinHash and Locality Sensitive Hashing (LSH) to estimate Jaccard similarity among documents and to identify near-duplicate documents. Given a collection of documents D = {D<sub>1</sub>, ... ,D<sub>N</sub>, consisting of terms {t<sub>1</sub>, ... , t<sub>M</sub>, the term document matrix is an M x N matrix and this matrix contains all the information to compute Jaccard similarity of any two documents. More specifically, a K x N MinHash matrix is constructed to estimate similarity of any two documents, where K is
the number of random permutations.
