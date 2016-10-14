import * as types from '../constants/ActionTypes'
// import fetch from 'whatwg-fetch'


export const addList = (text) => ({type: types.ADD_LIST, text});
export const addListItem = (listId, productId, text) => ({type: types.ADD_LIST_ITEM, listId, productId, text});

export const deleteList = (listId) => ({type: types.DELETE_LIST, listId});
export const deleteListItem = (listId, productId) => ({type: types.DELETE_LIST_ITEM, listId, productId});

export const editList = (listId, text) => ({type: types.EDIT_LIST, listId, text})
export const editListItem = (listId, productId, text) => ({type: types.EDIT_LIST_ITEM, listId, productId, text});

export const completeListItem = (listId, productId) => ({type: types.COMPLETE_LIST_ITEM, listId, productId});
export const completeAll = (listId) => ({type: types.COMPLETE_ALL, listId});
export const clearCompleted = (listId) => ({type: types.CLEAR_COMPLETED, listId});
export const login = (user, pass) => ({type: types.LOGIN, user, pass});
