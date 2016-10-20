import React, {Component, PropTypes} from 'react'
import Header from '../components/Header'
import Lists from '../components/Lists'

export default class Home extends Component {

    static propTypes = {
        state: PropTypes.object.isRequired,
        actions: PropTypes.object.isRequired
    };


    render() {
        const {state, actions} = this.props;
        console.log(state);
        return (
            <div>
                {<Header addTodo={actions.addList}/>}
                {<Lists lists={state.lists} actions={actions}/>}
            </div>
        )
    }
}
