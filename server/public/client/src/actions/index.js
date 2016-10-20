import * as types from '../constants/ActionTypes'
import 'whatwg-fetch'

var basePath = "http://localhost:9000/api/";
let headers = {
    'Accept': 'application/json',
    'Content-Type': 'application/json'
};

function receiveLogin(json) {
    return {
        type: types.LOGIN_RECEIVED,
        userData: json
    }
}

function requestLogin(user, pass) {
    let url = basePath + 'login';
    let request = {login: user, password: pass, clientId: '1', grantType: 'password', clientSecret: 'secret'};
    return dispatch => {
        return fetch(url, {
            method: 'POST',
            headers: headers,
            body: JSON.stringify(request)
        })
            .then(response => response.json())
            .then(json => dispatch(receiveLogin(json)))
    }
}

export const addList = (text) => ({type: types.ADD_LIST, text});
export const addListItem = (listId, productId, text) => ({type: types.ADD_LIST_ITEM, listId, productId, text});

export const deleteList = (listId) => ({type: types.DELETE_LIST, listId});
export const deleteListItem = (listId, productId) => ({type: types.DELETE_LIST_ITEM, listId, productId});

export const editList = (listId, text) => ({type: types.EDIT_LIST, listId, text})
export const editListItem = (listId, productId, text) => ({type: types.EDIT_LIST_ITEM, listId, productId, text});

export const completeListItem = (listId, productId) => ({type: types.COMPLETE_LIST_ITEM, listId, productId});
export const completeAll = (listId) => ({type: types.COMPLETE_ALL, listId});
export const clearCompleted = (listId) => ({type: types.CLEAR_COMPLETED, listId});
export const loginUser = (user, pass) => dispatch => {
    return dispatch(requestLogin(user, pass));
};

