import React, {Component} from 'react';
import './layer-notify.css';


const NOTIFY_HEIGHT = 50;
const NOTIFY_BOTTOM = 20;


export class LayerNotifyOptions {
    text = 'Notification';
    actionText = null;
    action = () => {};
    timeout = 4000;
}


export default class LayerNotify extends Component {

    static notifies = [];
    static defaultOptions = new LayerNotifyOptions();
    static components = [];

    /**
     * Adds notification to the current queue
     * @param {LayerNotifyOptions|object} options
     */
    static addNotify(options) {
        options = Object.assign({}, LayerNotify.defaultOptions, options);
        LayerNotify.notifies.unshift(options);
        LayerNotify.broadcastState();

        window.setTimeout(() => {
            LayerNotify.notifies.splice(LayerNotify.notifies.indexOf(options), 1);
            LayerNotify.broadcastState();
        }, options.timeout);
    }

    static broadcastState() {
        LayerNotify.components.forEach(component => {
            component.setState({notifies: LayerNotify.notifies});
        });
    }

    constructor(props) {
        super(props);
        this.state = {notifies: []}
    }

    componentDidMount() {
        LayerNotify.components.push(this);
    }

    componentWillUnmount() {
        delete LayerNotify.components[LayerNotify.components.indexOf(this)];
    }

    renderNotifies() {
        return this.state.notifies.map((notify, idx) => {
            return (
                <div className="layer-notify__box" style={{bottom: NOTIFY_BOTTOM + idx * NOTIFY_HEIGHT}}>
                    <div className="layer-notify__content">
                        <div>{notify.text}</div>

                        {notify.actionText !== null &&
                            <div className="layer-notify__action">
                                <a onClick={notify.action} href="#cancel">
                                    {notify.actionText}
                                </a>
                            </div>
                        }
                    </div>
                </div>
            )
        });
    }

    render() {
        return (
            <div className="layer-notify">
                {this.renderNotifies()}
            </div>
        )
    }

}
