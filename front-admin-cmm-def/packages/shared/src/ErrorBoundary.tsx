import {Component, ErrorInfo, ReactNode} from 'react';
import InternalServerError from './InternalServerError';
interface Props {
  children?: ReactNode;
}

interface State {
  hasError: boolean;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
  };

  public static getDerivedStateFromError(_: Error): State {
    return { hasError: true };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    // log
    console.log(error, errorInfo);
  }

  public render() {
    if (this.state.hasError) {
      return <InternalServerError />;
    }

    return this.props.children && this.props.children as JSX.Element
    }
  }

export default ErrorBoundary;
