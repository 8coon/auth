import React, {Component} from 'react';
import './layer-notify.css';


const NOTIFY_HEIGHT = 50;
const NOTIFY_BOTTOM = 20;


export class LayerNotifyOptions {
    text = 'Notification';
    actionText = null;
    action = () => true;
    timeout = 400000;
    shouldAppear = false;
    shouldDisappear = false;
    key = null;
}


export default class LayerNotify extends Component {

    static notifies = [];
    static defaultOptions = new LayerNotifyOptions();
    static components = [];
    static notifyCount = 0;

    /**
     * Adds notification to the current queue
     * @param {LayerNotifyOptions|object} options
     */
    static addNotify(options) {
        options = Object.assign({}, LayerNotify.defaultOptions, options, {
            shouldAppear: true, key: LayerNotify.notifyCount++,
        });

        LayerNotify.notifies.unshift(options);
        LayerNotify.broadcastState();

        window.setTimeout(() => {
            LayerNotify.closeNotify(options);
        }, options.timeout);
    }

    static closeNotify(notify) {
        notify.shouldDisappear = true;
        LayerNotify.broadcastState();

        window.setTimeout(() => {
            LayerNotify.notifies.splice(LayerNotify.notifies.indexOf(notify), 1);
            LayerNotify.broadcastState();
        }, 350);
    }

    static broadcastState() {
        LayerNotify.components.forEach(component => {
            component.setState({notifies: LayerNotify.notifies});
        });
    }

    constructor(props) {
        super(props);
        this.state = {notifies: []};
    }

    componentDidMount() {
        LayerNotify.components.push(this);
    }

    componentWillUnmount() {
        delete LayerNotify.components[LayerNotify.components.indexOf(this)];
    }

    getActionHandler(notify) {
        return () => {
            notify.action.then && notify.action.then(result => {
                result === true && LayerNotify.closeNotify(notify);
            });

            if (typeof notify.action === 'function' && notify.action(notify) === true) {
                LayerNotify.closeNotify(notify);
            }
        };
    }

    renderNotifies() {
        return this.state.notifies.map((notify, idx) => {
            const animations = [];

            if (notify.shouldAppear) {
                animations.push('0.4s notify-appear');
            }

            if (notify.shouldDisappear) {
                animations.push('0.4s notify-disappear');
            }

            return (
                <div key={notify.key}
                     className="layer-notify__box"
                     style={{
                          bottom: NOTIFY_BOTTOM + idx * NOTIFY_HEIGHT,
                          animation: animations.join(','),
                     }}>
                    <div className="layer-notify__content">
                        <div className="layer-notify__text">{notify.text}</div>

                        {notify.actionText !== null &&
                            <div className="layer-notify__action">
                                <a onClick={this.getActionHandler(notify)} href="#cancel">
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
