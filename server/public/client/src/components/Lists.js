import React, {Component, PropTypes} from 'react'
import ListItem from './ListItem'


export default class Lists extends Component {
    static propTypes = {
        lists: PropTypes.array.isRequired,
        actions: PropTypes.object.isRequired
    };

    render() {
        const {lists, actions} = this.props;
        console.log(this.props)

        return (
            <section className="main">
                <ul className="todo-list">
                    {lists.map(list =>
                        <ListItem key={list.clientId} item={list} {...actions} />
                    )}
                </ul>
            </section>
        )
    }
}
