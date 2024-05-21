type Props = {
  label: string;
  placeholder: string;
  value: string;
  onChange: (value: string) => void;
};
export const TwoLevelInput = (props: Props) => {
  const { label, placeholder, value, onChange } = props;
  return (
    <div className="flex flex-col">
      <label>{label}</label>
      <input
        className="border-2 border-gray-300 rounded-md w-full"
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
      />
    </div>
  );
};
