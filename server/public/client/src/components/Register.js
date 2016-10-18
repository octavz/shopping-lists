import React, {Component, PropTypes} from 'react'

export default class Register extends Component {
    static propTypes = {
        userData: PropTypes.object.isRequired,
        actions: PropTypes.object.isRequired
    };

    handleLogin = e => {
        console.log("doing register")
    };

    state = {
        login: '', password: '', password1: '', isAuthenticated: false, passValid: true
    };

    handleLoginChange = e => {
        this.setState({...this.state, login: e.target.value});
    };

    handlePassChange = e => {
        this.setState({
            ...this.state,
            password: e.target.value,
            passValid: e.target.value === this.state.password1
        });
    };

    handlePassConfirmChange = e => {
        this.setState({
            ...this.state,
            password1: e.target.value,
            passValid: e.target.value === this.state.password
        });
    };

    render() {
        return (
            <form className="form-horizontal">
                <div className="form-group">
                    <label htmlFor="inputEmail3" className="col-sm-2 control-label">Email</label>
                    <div className="col-sm-10">
                        <input type="email"
                               className="form-control"
                               id="email"
                               placeholder="Email"
                               value={this.state.login}
                               onChange={this.handleLoginChange}/>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="inputPassword3" className="col-sm-2 control-label">Password</label>
                    <div className={this.state.passValid ? "col-sm-10" : "col-sm-10 has-error"}>
                        <input type="password"
                               className="form-control"
                               id="password"
                               placeholder="Password"
                               value={this.state.password}
                               onChange={this.handlePassChange}/>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="inputPassword3" className="col-sm-2 control-label">Confirm Password</label>
                    <div className={this.state.passValid ? "col-sm-10" : "col-sm-10 has-error"}>
                        <input type="password"
                               className="form-control"
                               id="confirmPassword"
                               placeholder="Password"
                               value={this.state.password1}
                               onChange={this.handlePassConfirmChange}/>
                    </div>
                </div>
                <div className="form-group">
                    <div className="col-sm-offset-2 col-sm-10">
                        <button type="button" onClick={this.handleLogin} className="btn btn-default">Register</button>
                    </div>
                </div>
            </form>
        )
    }
}
