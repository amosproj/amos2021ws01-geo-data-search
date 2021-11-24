import { ChangeEvent } from "react";

type Props = {
  onChange: (value: string) => void;
  value: string;
  placeholder?: string;
};

const SearchInput = ({ value, onChange, placeholder }: Props) => {
  const onInputChange = (e: ChangeEvent<HTMLInputElement>) => {
    onChange?.(e.target.value);
  };

  return (
    <input
      className="border-solid border-gray-500 border rounded p-2 w-full"
      name="searchValue"
      type="text"
      placeholder={placeholder}
      value={value}
      onChange={onInputChange}
    />
  );
};

export default SearchInput;
