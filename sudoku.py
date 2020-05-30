#global
grid_size = 81

def trialCelli(grid):
	#find cell with value 0
	for i in range(grid_size):
		if grid[i] ==0 :
			print('Trialing Cell', i)
			return i

def isFull(grid):
	#check for count of zeros
	return grid.count(0) == 0

def setCell(val, trialCell, grid):
	grid[trialCell] = val
	return grid

def clearCell(trialCell, grid):
	#set the cell value to 0
	grid[trialCell] = 0
	print('Clearing cell', trialCell)
	return grid

def isValid(trialVal, trialCell, grid):
	#validate square
	cols = 0
	for eachsq in range(9):
		trailSq = [x+cols for x in range(3)] + [x+9+cols for x in range(3)] + [x+18+cols for x in range(3)]
		cols+=3

		if cols in [9, 36]:
			cols+=18

		if trialCell in trailSq:
			for i in trailSq:
				if grid[i]!=0:
					if int(grid[i]) == trialVal:
						print('Square', i)
						return False
		
	#validate row
	for eachrow in range(9):
		trialRow = [(9*eachrow)+x for x in range(9)]
		if trialCell in trialRow:
			for row in trialRow:
				if grid[row]!=0:
					if int(grid[row]) == trialVal:
						print('Row', row)
						return False

	#validate cols
	for eachcol in range(9):
		trialcols = [(9*x)+eachcol for x in range(9)]
		if trialCell in trialcols:
			for col in trialcols:
				if grid[col]!=0:
					if int(grid[col]) == trialVal:
						print('Column', col)
						return False

	#validated,can be placed
	print('Is legal',trialVal, 'can be placed on', trialCell)
	return True


def hasSolution(grid):
	if isFull(grid):
		print()
		print('\n SOLVED')
		return True
	else:
		solution_found = False
		trialVal = 1
		trialCell = trialCelli(grid)

		while (solution_found!=True) and (trialVal < 10):
			print('Trial Val', trialVal)
			if isValid(trialVal, trialCell, grid):
				#set the trialCell with trialVal
				grid = setCell(trialVal, trialCell, grid)
				if hasSolution(grid) == True:
					solution_found = True
					return True
				else:
					grid = clearCell(trialCell, grid)

			print('++')
			trialVal+=1
	return solution_found

def printGrid(grid):
	i = 0
	l = 9
	for j in range(9):
		print()
		while i < l:
			print(grid[i], end = ' ')
			i+=1
		l+=9


def main():
	sampleGrid = [5, 3, 0 ,0, 7, 0, 0, 3, 0,
				  6, 0, 0 ,1, 9, 5, 0, 0, 0,
				  0, 9, 8 ,0, 0, 0, 0, 6, 0,
				  8, 0, 0 ,0, 6, 0, 0, 0, 3,
				  4, 0, 0 ,8, 0, 3, 0, 0, 1,
				  7, 0, 0 ,0, 2, 0, 0, 0, 6,
				  0, 6, 0 ,0, 0, 0, 2, 8, 0,
				  0, 0, 0 ,4, 1, 9, 0, 0, 5,
				  0, 0, 0 ,0, 8, 0, 0, 7, 9,]


	printGrid(sampleGrid)
	if hasSolution(sampleGrid):
		printGrid(sampleGrid)
	else:
		print('No Solution')

main()


'''
8, 9, 2 ,0, 0, 5, 0, 0, 3,
0, 0, 5 ,1, 3, 0, 0, 4, 0,
1, 4, 0 ,7, 0, 8, 5, 0, 0,
0, 6, 0 ,0, 0, 0, 4, 0, 5,
0, 1, 0 ,0, 0, 0, 0, 3, 0,
7, 0, 9 ,0, 0, 0, 0, 8, 0,
0, 0, 6 ,8, 0, 1, 0, 9, 0,
0, 3, 0 ,0, 6, 4, 7, 0, 0,	
4, 0, 0 ,3, 0, 0, 2, 6, 1,
'''