import React, {Component, PropTypes} from 'react'
import Header from '../components/Header'
import Login from '../components/Login'
import Lists from '../components/Lists'

export default class Home extends Component {
    static propTypes = {
        data: PropTypes.object.isRequired,
        actions: PropTypes.object.isRequired
    }

    render() {
        const login = false;
        const {data, actions} = this.props;
        if (login) {
            return (
                <Login/>
            )
        } else {
            return (
                <div>
                    {<Header addTodo={actions.addList}/>}
                    {<Lists lists={data.lists} actions={actions}/>}
                </div>
            )
        }
    }
}
