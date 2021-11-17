type Props = {
  message: string;
};

const ErrorMessage = ({ message }: Props) => {
  return <p className="text-red-500 mb-2">{message}</p>;
};

export default ErrorMessage;
