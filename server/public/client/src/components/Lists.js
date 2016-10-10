import React, {Component, PropTypes} from 'react'
import ListItem from './ListItem'


export default class Lists extends Component {
    static propTypes = {
        lists: PropTypes.array.isRequired,
        actions: PropTypes.object.isRequired
    }

    render() {
        console.log(this.props);
        const {lists, actions} = this.props;

        return (
            <section className="main">
                <ul className="todo-list">
                    {lists.map(list =>
                        <ListItem key={list.id} item={list} {...actions} />
                    )}
                </ul>
            </section>
        )
    }
}
