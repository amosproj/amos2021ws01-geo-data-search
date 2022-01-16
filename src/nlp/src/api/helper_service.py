from array import array
import numpy as np

def convert_number_to_meter(unit: str, number: int) -> int:
    """
    Converts input number into meter
    :param unit Unit of number
    :param number Integer to be converted
    """
    if unit == "m":
        return number
    if unit == "miles":
        return int(number * 1609.34)
    # assumed default unit is km
    return number * 1000

def check_similarity_in_list(token_1: str, token_list: array, threshold: int) -> bool:
    """ 
    :param token_1
    :param token_list
    :return true, if token_1 and at least one token in token_list are equal due to levenshtein distance and a given threshold, otherwise false 
    """
    
    for token in token_list:
        if check_similarity(token_1, token, threshold):
            return True
    return False

def check_similarity(token_1: str, token_2: str, threshold: int) -> bool:
    """ 
    :param token_1
    :param token_2
    :return true, if tokens are equal due to levenshtein distance and a given threshold, otherwise false
    """
    
    distance_ratio = compute_levenshtein_distance(token_1,token_2)
    if distance_ratio > threshold:
        return True
    else:
        return False

def compute_levenshtein_distance(token_1: str, token_2: str)-> int:
    """ 
    Calculates levenshtein distance between two tokens.
    :param token_1
    :param token_2
    :return levenshtein distance ratio
    """
    # initialize matrix 
    rows = len(token_1)+1
    cols = len(token_2)+1
    distance = np.zeros((rows,cols),dtype = int)

    # calculate matrix with the indeces of each character of both tokens
    for i in range(1, rows):
        for k in range(1,cols):
            distance[i][0] = i
            distance[0][k] = k

    # iterate over the matrix to compute the cost   
    for col in range(1, cols):
        for row in range(1, rows):
            # if the characters are the same in the two strings in a given position [i,j]
            if token_1[row-1] == token_2[col-1]:
                cost = 0 # then the cost is 0
            else:
                cost = 1 # otherwise the cost is 1 
            distance[row][col] = min(distance[row-1][col] + 1,      # deletion cost
                                 distance[row][col-1] + 1,          # insertion cost
                                 distance[row-1][col-1] + cost)     # substitution cost
    # compute final Levenshtein Distance Ratio
    distance_ratio = ((len(token_1)+len(token_2)) - distance[row][col]) / (len(token_1)+len(token_2))
    return distance_ratio
 