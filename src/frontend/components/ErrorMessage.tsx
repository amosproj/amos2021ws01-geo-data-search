type Props = {
  message: string;
};

const ErrorMessage = ({ message }: Props) => {
  return <p className="text-red-500 mb-2 whitespace-pre-wrap overflow-x-auto bg-white rounded p-3">{message}</p>;
};

export default ErrorMessage;
