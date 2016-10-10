import {
    ADD_LIST,
    DELETE_LIST,
    DELETE_LIST_ITEM,
    EDIT_LIST,
    COMPLETE_LIST_ITEM,
    COMPLETE_ALL,
    CLEAR_COMPLETED
} from '../constants/ActionTypes'

const initialState = {
    userData: {
        key: "auth-key",
        email: "user@email.com",
        name: "Popescu Ion"
    },
    lists: [
        {
            id: "111",
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

export default function items(state = initialState, action) {
    var currentList = () => {
        var {lists} = state;
        return lists.filter(l => l.id === action.listId)[0]
    };

    switch (action.type) {
        case DELETE_LIST:
            console.log("delete list");
            return ({
                ...state,
                lists: state.lists.filter(l => l.id !== action.listId),
            })
        case ADD_LIST:
            return {
                lists: [currentList().push({
                    text: action.text,
                    productId: action.productId
                }), ...state.lists.filter(l => l.id !== action.listId)],
                ...state
            };
        case DELETE_LIST_ITEM:
            console.log(action);
            return state;
        // return state.filter(todo =>
        //     todo.id !== action.id
        // )

        case EDIT_LIST:
            console.log(action);
            return state;
        // return state.filter(todo =>
        // return state.map(todo =>
        //     todo.id === action.id ?
        //     {...todo, text: action.text} :
        //         todo
        // )

        case COMPLETE_LIST_ITEM:
            console.log(action);
            return state;
        // return state.filter(todo =>
        // return state.map(todo =>
        //     todo.id === action.id ?
        //     {...todo, completed: !todo.completed} :
        //         todo
        // )

        case COMPLETE_ALL:
            console.log(action);
            return state;
        // const areAllMarked = state.every(todo => todo.completed)
        // return state.map(todo => ({
        //     ...todo,
        //     completed: !areAllMarked
        // }))

        case CLEAR_COMPLETED:
            console.log(action);
            return state;
        //return state.filter(todo => todo.completed === false)

        default:
            return state
    }
}
