import React, {Component, PropTypes} from 'react'
import Header from '../components/Header'
import Lists from '../components/Lists'

export default class Home extends Component {
    static propTypes = {
        lists: PropTypes.object.isRequired,
        actions: PropTypes.object.isRequired
    };

    render() {
        const {lists, actions} = this.props;
        return (
            <div>
                {<Header addTodo={actions.addList}/>}
                {<Lists lists={lists} actions={actions}/>}
            </div>
        )
    }
}
