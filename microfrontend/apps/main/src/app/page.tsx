// app/store/page.tsx (this is a SERVER component by default)

export const metadata = {
  title: "Store | Kitchen Sink",
};

import Store from './store-client'; // rename your current file to store-client.tsx

export default function StorePage() {
  return <Store />;
}
