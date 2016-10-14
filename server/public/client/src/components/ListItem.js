import React, {Component, PropTypes} from 'react'
import classnames from 'classnames'
import ListTextInput from './ListTextInput'

export default class ListItem extends Component {
    static propTypes = {
        item: PropTypes.object.isRequired,
        editList: PropTypes.func.isRequired,
        deleteList: PropTypes.func.isRequired
    };

    state = {
        editing: false
    };

    handleClick = () => {
        this.setState({editing: true})
    };

    handleDoubleClick = () => {
        this.setState({editing: true})
    };

    handleSave = (id, text) => {
        if (text.length === 0) {
            this.props.deleteList(id)
        } else {
            this.props.editList(id, text)
        }
        this.setState({editing: false})
    };

    render() {
        const {item, deleteList} = this.props;

        let element;
        if (this.state.editing) {
            element = (
                <ListTextInput text={item.name}
                               editing={this.state.editing}
                               onSave={(text) => this.handleSave(item.id, text)}/>
            )
        } else {
            element = (
                <div className="view">
                    <label onDoubleClick={this.handleDoubleClick} onClick={this.handleClick}>
                        {item.name}
                    </label>
                    <button className="destroy"
                            onClick={() => deleteList(item.id)}/>
                </div>
            )
        }

        return (
            <li className={classnames({
                completed: item.completed,
                editing: this.state.editing
            })}>
                {element}
            </li> )
    }
}
