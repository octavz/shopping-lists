import * as types from '../constants/ActionTypes'

const initialState = {
    userData: {
        key: "auth-key",
        email: "user@email.com",
        name: "Popescu Ion",
        loggedIn: false
    },
    lists: [
        {
            id: "111",
            clientId: "111",
            name: 'My list no 1',
            items: [
                {
                    text: 'Cartofi',
                    completed: false,
                    productId: 0
                },
                {
                    text: 'Varza',
                    completed: false,
                    productId: 1
                }
            ]
        },
        {
            id: "112",
            clientId: "112",
            name: 'My list no 2',
            items: [
                {
                    text: 'Ciocolata',
                    completed: false,
                    productId: 0
                }
            ]
        }
    ]
}

export function guid() {
    function s4() {
        return Math.floor((1 + Math.random()) * 0x10000)
            .toString(16)
            .substring(1);
    }

    return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
        s4() + '-' + s4() + s4() + s4();
}

export default function items(state = initialState, action) {
    switch (action.type) {
        case types.DELETE_LIST:
            console.log("delete list");
            return ({
                ...state,
                lists: state.lists.filter(l => l.id !== action.listId),
            });
        case types.ADD_LIST:
            return {
                userData: state.userData,
                lists: [...state.lists, {clientId: guid(), name: action.text}]
            };
        case types.DELETE_LIST_ITEM:
            console.log(action);
            return state;
        // return state.filter(todo =>
        //     todo.id !== action.id
        // )

        case types.EDIT_LIST:
            console.log(action);
            return state;
        // return state.filter(todo =>
        // return state.map(todo =>
        //     todo.id === action.id ?
        //     {...todo, text: action.text} :
        //         todo
        // )

        case types.COMPLETE_LIST_ITEM:
            console.log(action);
            return state;
        // return state.filter(todo =>
        // return state.map(todo =>
        //     todo.id === action.id ?
        //     {...todo, completed: !todo.completed} :
        //         todo
        // )

        case types.COMPLETE_ALL:
            console.log(action);
            return state;
        // const areAllMarked = state.every(todo => todo.completed)
        // return state.map(todo => ({
        //     ...todo,
        //     completed: !areAllMarked
        // }))

        case types.CLEAR_COMPLETED:
            console.log(action);
            return state;
        //return state.filter(todo => todo.completed === false)
        case types.LOGIN_RECEIVED:
            console.log(action);
            return {...state, userData: action.userData};
        default:
            return state
    }
}
